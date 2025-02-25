package de.schoenfeld.chess;

import java.util.ArrayList;
import java.util.List;

public class SimpleMoveHistory implements MoveHistory {
    private final List<Move> moveHistory;

    public SimpleMoveHistory() {
        moveHistory = new ArrayList<>();
    }

    @Override
    public void recordMove(Move move) {
        moveHistory.addFirst(move);
    }

    @Override
    public Move getLastMove() {
        return moveHistory.getLast();
    }

    public Move undoLastMove() {
        Move undoneMove = moveHistory.getLast();
        moveHistory.removeLast();
        return undoneMove;
    }

    @Override
    public List<Move> getAllMoves() {
        return moveHistory;
    }

    @Override
    public int getMoveCount() {
        return moveHistory.size();
    }
}
