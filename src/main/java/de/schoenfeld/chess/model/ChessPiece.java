package de.schoenfeld.chess.model;

import java.io.Serializable;

public record ChessPiece<T extends PieceType>(
        T pieceType,
        boolean isWhite,
        boolean hasMoved
) implements Serializable {

    public ChessPiece(T pieceType, boolean isWhite) {
        this(pieceType, isWhite, false);
    }

    public ChessPiece<T> withIsWhiteSwitched() {
        return new ChessPiece<>(pieceType, !isWhite, hasMoved);
    }

    public ChessPiece<T> withMoved() {
        return new ChessPiece<>(pieceType, isWhite, true);
    }
}