package de.schoenfeld.chesskit.move.components;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;

import java.io.Serial;
import java.util.Objects;

public class CaptureComponent<T extends Tile, P extends PieceType> implements MoveComponent<T, P> {
    @Serial
    private static final long serialVersionUID = -5357299845193533258L;
    protected final ChessPiece<P> capturedPiece;

    public CaptureComponent(ChessPiece<P> capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    public ChessPiece<P> capturedPiece() {
        return capturedPiece;
    }

    @Override
    public void makeOn(GameState<T, P> gameState, Move<T, P> move) {
    }

    @Override
    public void unmakeOn(GameState<T, P> gameState, Move<T, P> move) {
        gameState.setPieceAt(move.to(), capturedPiece);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        CaptureComponent<?, ?> that = (CaptureComponent<?, ?>) o;
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
