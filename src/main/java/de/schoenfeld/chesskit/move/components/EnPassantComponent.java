package de.schoenfeld.chesskit.move.components;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;

import java.io.Serial;

public class EnPassantComponent<T extends Tile, P extends PieceType> extends CaptureComponent<T, P> {
    @Serial
    private static final long serialVersionUID = -6540828058731930581L;
    private final T enPassantSquare8x8;

    public EnPassantComponent(ChessPiece<P> capturedPiece, T enPassantSquare8x8) {
        super(capturedPiece);
        this.enPassantSquare8x8 = enPassantSquare8x8;
    }

    @Override
    public void unmakeOn(GameState<T, P> gameState, Move<T, P> move) {
        gameState.setPieceAt(enPassantSquare8x8, capturedPiece);
    }

    @Override
    public void makeOn(GameState<T, P> gameState, Move<T, P> move) {
        gameState.removePieceAt(enPassantSquare8x8);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(getClass())) return false;

        EnPassantComponent<?, ?> other = (EnPassantComponent<?, ?>) o;

        return this.capturedPiece.equals(other.capturedPiece);
    }

    @Override
    public String toString() {
        return "EnPassantComponent{" +
                "capturedPiece=" + capturedPiece +
                '}';
    }
}
