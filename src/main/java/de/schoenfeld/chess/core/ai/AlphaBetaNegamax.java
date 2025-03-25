package de.schoenfeld.chess.core.ai;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.rules.Rules;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AlphaBetaNegamax<T extends PieceType> implements MoveSearchStrategy<T> {
    private static final int INF = Integer.MAX_VALUE;
    private final int maxDepth;
    private final int parallelDepth;
    private final Rules<T> rules;
    private final GameStateEvaluator<T> evaluator;
    private final ExecutorService rootExecutor;
    private final ForkJoinPool branchPool;
    private final Map<Long, TranspositionEntry> transpositionTable = new ConcurrentHashMap<>();
    private final MoveOrderingHeuristic<T> heuristic;

    public AlphaBetaNegamax(int maxDepth, int parallelDepth, Rules<T> rules,
                            GameStateEvaluator<T> evaluator, MoveOrderingHeuristic<T> heuristic) {
        this.maxDepth = maxDepth;
        this.parallelDepth = parallelDepth;
        this.heuristic = heuristic;
        this.rules = rules;
        this.evaluator = evaluator;
        this.rootExecutor = Executors.newWorkStealingPool();
        this.branchPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
    }

    public static <T extends PieceType> Builder<T> builder() {
        return new Builder<>();
    }

    @Override
    public Move<T> searchMove(GameState<T> gameState) {
        List<Move<T>> legalMoves = rules.generateMoves(gameState);
        legalMoves.sort(Comparator.comparingInt(heuristic).reversed());

        AtomicInteger alpha = new AtomicInteger(-INF);
        AtomicInteger beta = new AtomicInteger(INF);
        AtomicReference<Move<T>> bestMove = new AtomicReference<>();

        List<CompletableFuture<Void>> futures = legalMoves.stream()
                .map(move -> CompletableFuture.runAsync(() -> {
                    move.executeOn(gameState);
                    int moveValue = -branchPool.invoke(new NegamaxTask(gameState, maxDepth - 1, -beta.get(), -alpha.get(), parallelDepth));

                    synchronized (alpha) {
                        if (moveValue > alpha.get()) {
                            alpha.set(moveValue);
                            bestMove.set(move);
                        }
                    }

                    move.undoOn(gameState);
                }, rootExecutor))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return bestMove.get() != null ? bestMove.get() : legalMoves.getFirst();
    }

    private record TranspositionEntry(int score, int depth) {
    }

    public static class Builder<T extends PieceType> {
        private int maxDepth = 3;
        private int parallelDepth = 1;
        private Rules<T> rules;
        private GameStateEvaluator<T> evaluator;
        private MoveOrderingHeuristic<T> heuristic;

        public Builder<T> maxDepth(int maxDepth) {
            if (maxDepth <= 0)
                throw new IllegalArgumentException("maxDepth has to be at least 1");
            this.maxDepth = maxDepth;
            return this;
        }

        public Builder<T> parallelDepth(int parallelDepth) {
            if (parallelDepth <= 0)
                throw new IllegalArgumentException("parallelDepth has to be at least 1");
            this.parallelDepth = parallelDepth;
            return this;
        }

        public Builder<T> rules(Rules<T> rules) {
            if (rules == null)
                throw new NullPointerException("rules");
            this.rules = rules;
            return this;
        }

        public Builder<T> evaluator(GameStateEvaluator<T> evaluator) {
            if (evaluator == null)
                throw new NullPointerException("evaluator");
            this.evaluator = evaluator;
            return this;
        }

        public Builder<T> heuristic(MoveOrderingHeuristic<T> heuristic) {
            if (heuristic == null)
                throw new NullPointerException("heuristic");
            this.heuristic = heuristic;
            return this;
        }

        public AlphaBetaNegamax<T> build() {
            if (maxDepth <= 0)
                throw new IllegalStateException("maxDepth has to be at least 1");
            if (parallelDepth <= 0)
                throw new IllegalStateException("parallelDepth has to be at least 1");
            if (rules == null)
                throw new NullPointerException("rules");
            if (evaluator == null)
                throw new NullPointerException("evaluator");
            if (heuristic == null)
                throw new NullPointerException("heuristic");
            return new AlphaBetaNegamax<>(maxDepth, parallelDepth, rules, evaluator, heuristic);
        }
    }

    private class NegamaxTask extends RecursiveTask<Integer> {
        @Serial
        private static final long serialVersionUID = 1L;
        private final GameState<T> state;
        private final int depth;
        private final int alpha;
        private final int beta;
        private final int currentParallelDepth;

        NegamaxTask(GameState<T> state, int depth, int alpha, int beta, int currentParallelDepth) {
            this.state = state;
            this.depth = depth;
            this.alpha = alpha;
            this.beta = beta;
            this.currentParallelDepth = currentParallelDepth;
        }

        @Override
        protected Integer compute() {
            long hash = state.hashCode();
            if (transpositionTable.containsKey(hash)) {
                TranspositionEntry entry = transpositionTable.get(hash);
                if (entry.depth >= depth) {
                    return entry.score;
                }
            }

            if (depth == 0) {
                return evaluator.evaluate(state);
            }

            List<Move<T>> moves = rules.generateMoves(state);
            moves.sort(Comparator.comparingInt(heuristic).reversed());

            int bestValue = -INF;
            int currentAlpha = alpha;

            List<NegamaxTask> tasks = new ArrayList<>();
            for (Move<T> move : moves) {
                move.executeOn(state);
                if (depth > (maxDepth - currentParallelDepth)) {
                    int value = -new NegamaxTask(new GameState<>(), depth - 1, -beta, -currentAlpha, currentParallelDepth).compute();
                    bestValue = Math.max(bestValue, value);
                    currentAlpha = Math.max(currentAlpha, bestValue);
                    if (currentAlpha >= beta) break;
                } else {
                    NegamaxTask task = new NegamaxTask(new GameState<>(), depth - 1, -beta, -currentAlpha, currentParallelDepth);
                    tasks.add(task);
                    task.fork();
                }
                move.undoOn(state);
            }

            for (NegamaxTask task : tasks) {
                int value = -task.join();
                bestValue = Math.max(bestValue, value);
                currentAlpha = Math.max(currentAlpha, bestValue);
                if (currentAlpha >= beta) {
                    tasks.stream().filter(t -> t != task && !t.isDone()).forEach(t -> t.cancel(false));
                    break;
                }
            }

            transpositionTable.put(hash, new TranspositionEntry(bestValue, depth));
            return bestValue;
        }
    }
}
