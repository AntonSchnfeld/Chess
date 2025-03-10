package de.schoenfeld.chess.model;

import de.schoenfeld.chess.move.Move;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record MoveHistory(
        List<Move> moves // Immutable list
) implements Serializable {

    public MoveHistory {
        moves = List.copyOf(moves); // Defensive copy
    }

    public MoveHistory() {
        this(List.of());
    }

    public MoveHistory withMoveRecorded(Move move) {
        List<Move> newMoves = new ArrayList<>(moves);
        newMoves.add(move);
        return new MoveHistory(newMoves);
    }

    public MoveHistory withoutLastMove() {
        if (moves.isEmpty()) return this;
        return new MoveHistory(moves.subList(0, moves.size() - 1));
    }

    public boolean isDrawBy50MoveRule() {
        int counter = 0;
        // Check last 100 plies (50 full moves)
        for (int i = moves.size() - 1; i >= Math.max(0, moves.size() - 100); i--) {
            Move move = moves.get(i);
            if (isResetMove(move)) {
                return counter >= 50;
            }
            counter++;
        }
        return counter >= 50;
    }

    private boolean isResetMove(Move move) {
        return move.movedPiece().getPieceType() == PieceType.PAWN ||
                move.isCapture();
    }

    public Move getLastMove() {
        return moves.isEmpty() ? null : moves.getLast();
    }

    public List<Move> getAllMoves() {
        return Collections.unmodifiableList(moves);
    }

    public int getMoveCount() {
        return moves.size();
    }
}