package de.schoenfeld.chesskit.move.components;

import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;

import java.util.Objects;

public class CaptureComponent<T extends PieceType> implements MoveComponent<T> {
    protected final ChessPiece<T> capturedPiece;

    public CaptureComponent(ChessPiece<T> capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    public ChessPiece<T> capturedPiece() {
        return capturedPiece;
    }

    @Override
    public void executeOn(GameState<T> gameState, Move<T> move) {
    }

    @Override
    public void undoOn(GameState<T> gameState, Move<T> move) {
        gameState.setPieceAt(move.to(), capturedPiece);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        CaptureComponent<?> that = (CaptureComponent<?>) o;
        return Objects.equals(capturedPiece, that.capturedPiece);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(capturedPiece);
    }

    @Override
    public String toString() {
        return "CaptureComponent{" +
                "capturedPiece=" + capturedPiece +
                '}';
    }
}
