package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AlphaBetaNegamax<T extends Tile, P extends PieceType> implements MoveSearchStrategy<T, P> {
    private static final int INF = Integer.MAX_VALUE;
    private final int maxDepth;
    private final GameStateEvaluator<T, P> evaluator;
    private final Map<Long, TranspositionEntry> transpositionTable;
    private final MoveOrderingHeuristic<T, P> heuristic;

    public AlphaBetaNegamax(int maxDepth,
                            GameStateEvaluator<T, P> evaluator,
                            MoveOrderingHeuristic<T, P> heuristic) {
        this.maxDepth = maxDepth;
        this.evaluator = evaluator;
        this.heuristic = heuristic;
        this.transpositionTable = new HashMap<>();
    }

    @Override
    public Move<T, P> searchMove(GameState<T, P> gameState) {
        MoveLookup<T, P> legalMoves = gameState.getMoves();
        legalMoves.sort(Comparator.comparingInt(heuristic).reversed());

        int alpha = -INF;
        int beta = INF;
        Move<T, P> bestMove = null;
        for (Move<T, P> move : legalMoves) {
            gameState.makeMove(move);
            int moveValue = -negamax(gameState, maxDepth - 1, -beta, -alpha);
            if (moveValue > alpha) {
                alpha = moveValue;
                bestMove = move;
            }
            gameState.unmakeLastMove();
        }

        return bestMove != null ? bestMove : legalMoves.getFirst();  // fallback if no best move found
    }

    private int negamax(GameState<T, P> state, int depth, int alpha, int beta) {
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

        /*
        FIXME This MoveLookup will get released when the next depth is calculated because it is
        cleared for state.getMoves();
         */
        MoveLookup<T, P> moves = state.getMoves();
        moves.sort(Comparator.comparingInt(heuristic).reversed());
        int bestValue = -INF;
        for (Move<T, P> move : moves) {
            state.makeMove(move);

            int value = -negamax(state, depth - 1, -beta, -alpha);
            bestValue = Math.max(bestValue, value);
            alpha = Math.max(alpha, bestValue);
            if (alpha >= beta) {
                break;  // prune branch
            }

            state.unmakeLastMove();
        }

        transpositionTable.put(hash, new TranspositionEntry(bestValue, depth));
        return bestValue;
    }

    private record TranspositionEntry(int score, int depth) {
    }
}
