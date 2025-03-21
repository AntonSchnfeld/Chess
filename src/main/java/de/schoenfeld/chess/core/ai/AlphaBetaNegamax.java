package de.schoenfeld.chess.core.ai;

import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.components.CaptureComponent;
import de.schoenfeld.chess.move.components.PromotionComponent;
import de.schoenfeld.chess.rules.Rules;

import java.io.Serial;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AlphaBetaNegamax implements MoveSearchStrategy {
    private final int maxDepth;
    private final int parallelDepth;
    private final Rules rules;
    private final GameStateEvaluator evaluator;
    private final ExecutorService rootExecutor;
    private final ForkJoinPool branchPool;
    private final Map<Long, TranspositionEntry> transpositionTable = new ConcurrentHashMap<>();
    private GameState lastState;


    private static final int INF = Integer.MAX_VALUE;
    private static final int MATE_SCORE = INF - 100;

    public AlphaBetaNegamax(int maxDepth, int parallelDepth, Rules rules, GameStateEvaluator evaluator) {
        this.maxDepth = maxDepth;
        this.parallelDepth = parallelDepth;
        this.rules = rules;
        this.evaluator = evaluator;
        this.rootExecutor = Executors.newWorkStealingPool();
        this.branchPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public Move searchMove(GameState gameState) {
        lastState = gameState;
        List<Move> legalMoves = rules.generateMoves(gameState);
        legalMoves.sort(Comparator.comparingInt(this::moveOrderingHeuristic).reversed());

        AtomicInteger alpha = new AtomicInteger(-INF);
        AtomicInteger beta = new AtomicInteger(INF);
        AtomicReference<Move> bestMove = new AtomicReference<>();

        List<CompletableFuture<Void>> futures = legalMoves.stream()
                .map(move -> CompletableFuture.runAsync(() -> {
                    GameState newState = move.executeOn(gameState);
                    int moveValue = -branchPool.invoke(new NegamaxTask(newState, maxDepth - 1, -beta.get(), -alpha.get(), parallelDepth));

                    synchronized (alpha) {
                        if (moveValue > alpha.get()) {
                            alpha.set(moveValue);
                            bestMove.set(move);
                        }
                    }
                }, rootExecutor))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return bestMove.get() != null ? bestMove.get() : legalMoves.getFirst();
    }

    private int moveOrderingHeuristic(Move move) {
        GameState newState = move.executeOn(lastState);

        // Check if the move wins the game
        Optional<GameConclusion> gameConclusion = rules.detectGameEndCause(newState);
        if (gameConclusion.isPresent()) {
            boolean myColour = newState.isWhiteTurn();
            GameConclusion val = gameConclusion.get();

            if (val.isDraw()) return -10_000;
            else return GameConclusion.Winner.of(myColour).equals(val.winner()) ?
                    Integer.MAX_VALUE :
                    Integer.MIN_VALUE;
        }

        int score = 0;

        if (move.hasComponent(PromotionComponent.class)) {
            score += 1000; // Prioritize promotions
        }

        if (move.hasComponent(CaptureComponent.class)) {
            // Capture moves are prioritized based on piece value
            score += 100 + move.getComponent(CaptureComponent.class)
                    .capturedPiece()
                    .pieceType()
                    .value();
        }

        return score;
    }

    private class NegamaxTask extends RecursiveTask<Integer> {
        @Serial
        private static final long serialVersionUID = 1L;
        private final GameState state;
        private final int depth;
        private final int alpha;
        private final int beta;
        private final int currentParallelDepth;

        NegamaxTask(GameState state, int depth, int alpha, int beta, int currentParallelDepth) {
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

            List<Move> moves = rules.generateMoves(state);
            moves.sort(Comparator.comparingInt(AlphaBetaNegamax.this::moveOrderingHeuristic).reversed());

            int bestValue = -INF;
            int currentAlpha = alpha;

            List<NegamaxTask> tasks = new ArrayList<>();
            for (Move move : moves) {
                GameState newState = move.executeOn(state);
                if (depth > (maxDepth - currentParallelDepth)) {
                    int value = -new NegamaxTask(newState, depth - 1, -beta, -currentAlpha, currentParallelDepth).compute();
                    bestValue = Math.max(bestValue, value);
                    currentAlpha = Math.max(currentAlpha, bestValue);
                    if (currentAlpha >= beta) break;
                } else {
                    NegamaxTask task = new NegamaxTask(newState, depth - 1, -beta, -currentAlpha, currentParallelDepth);
                    tasks.add(task);
                    task.fork();
                }
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

    private record TranspositionEntry(int score, int depth) {
    }
}
