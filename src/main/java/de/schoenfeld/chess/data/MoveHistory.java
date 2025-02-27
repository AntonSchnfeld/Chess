package de.schoenfeld.chess.data;

import de.schoenfeld.chess.data.move.Move;
import de.schoenfeld.chess.logic.piece.PieceType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoveHistory {
    private final List<Move> moveHistory;
    private int movesSinceCaptureOrPawnMove;

    public MoveHistory(List<Move> moveHistory) {
        this.moveHistory = new ArrayList<>(moveHistory);
        movesSinceCaptureOrPawnMove = 0;
    }

    public MoveHistory() {
        this(new ArrayList<>());
    }

    public void recordMove(Move move) {
        moveHistory.addLast(move);
        if (move.movedPiece().getPieceType().equals(PieceType.PAWN)) movesSinceCaptureOrPawnMove = 0;
        else if (move.isCapture()) movesSinceCaptureOrPawnMove = 0;
        else movesSinceCaptureOrPawnMove++;
    }

    public boolean isDrawBy50MoveRule() {
        return movesSinceCaptureOrPawnMove >= 50;
    }

    public Move getLastMove() {
        return moveHistory.getLast();
    }

    public Move undoMove() {
        return moveHistory.removeLast();
    }

    public List<Move> getAllMoves() {
        return Collections.unmodifiableList(moveHistory);
    }

    public void clear() {
        moveHistory.clear();
    }

    public int getMoveCount() {
        return moveHistory.size();
    }
}