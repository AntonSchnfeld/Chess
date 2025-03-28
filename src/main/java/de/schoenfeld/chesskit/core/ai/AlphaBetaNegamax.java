package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.rules.Rules;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class AlphaBetaNegamax<T extends PieceType> implements MoveSearchStrategy<T> {
    private static final int INF = Integer.MAX_VALUE;
    private final int maxDepth;
    private final Rules<T> rules;
    private final GameStateEvaluator<T> evaluator;
    private final Map<Long, TranspositionEntry> transpositionTable = new java.util.HashMap<>();
    private final MoveOrderingHeuristic<T> heuristic;

    public AlphaBetaNegamax(int maxDepth, Rules<T> rules, GameStateEvaluator<T> evaluator, MoveOrderingHeuristic<T> heuristic) {
        this.maxDepth = maxDepth;
        this.rules = rules;
        this.evaluator = evaluator;
        this.heuristic = heuristic;
    }

    public static <T extends PieceType> Builder<T> builder() {
        return new Builder<>();
    }

    @Override
    public Move<T> searchMove(GameState<T> gameState) {
        List<Move<T>> legalMoves = rules.generateMoves(gameState);
        legalMoves.sort(Comparator.comparingInt(heuristic).reversed());

        int alpha = -INF;
        int beta = INF;
        Move<T> bestMove = null;

        for (Move<T> move : legalMoves) {
            move.executeOn(gameState);
            try {
                int moveValue = -negamax(gameState, maxDepth - 1, -beta, -alpha);
                if (moveValue > alpha) {
                    alpha = moveValue;
                    bestMove = move;
                }
            } finally {
                move.undoOn(gameState);
            }
        }

        return bestMove != null ? bestMove : legalMoves.getFirst();  // fallback if no best move found
    }

    private int negamax(GameState<T> state, int depth, int alpha, int beta) {
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
        for (Move<T> move : moves) {
            move.executeOn(state);
            try {
                int value = -negamax(state, depth - 1, -beta, -alpha);
                bestValue = Math.max(bestValue, value);
                alpha = Math.max(alpha, bestValue);
                if (alpha >= beta) {
                    break;  // prune branch
                }
            } finally {
                move.undoOn(state);
            }
        }

        transpositionTable.put(hash, new TranspositionEntry(bestValue, depth));
        return bestValue;
    }

    private record TranspositionEntry(int score, int depth) {
    }

    public static class Builder<T extends PieceType> {
        private int maxDepth = 3;
        private Rules<T> rules;
        private GameStateEvaluator<T> evaluator;
        private MoveOrderingHeuristic<T> heuristic;

        public Builder<T> maxDepth(int maxDepth) {
            if (maxDepth <= 0)
                throw new IllegalArgumentException("maxDepth has to be at least 1");
            this.maxDepth = maxDepth;
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
            if (rules == null)
                throw new NullPointerException("rules");
            if (evaluator == null)
                throw new NullPointerException("evaluator");
            if (heuristic == null)
                throw new NullPointerException("heuristic");
            return new AlphaBetaNegamax<>(maxDepth, rules, evaluator, heuristic);
        }
    }
}
