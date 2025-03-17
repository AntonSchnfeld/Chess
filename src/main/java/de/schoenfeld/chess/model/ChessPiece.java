package de.schoenfeld.chess.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public record ChessPiece(
        PieceType pieceType,
        boolean isWhite,
        boolean hasMoved,
        long uniqueId
) implements Serializable {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    public ChessPiece(PieceType pieceType, boolean isWhite) {
        this(pieceType, isWhite, false, ID_GENERATOR.getAndIncrement());
    }

    public ChessPiece withMoved(boolean hasMoved) {
        if (hasMoved == this.hasMoved) return this;
        return new ChessPiece(pieceType, isWhite, hasMoved, ID_GENERATOR.getAndIncrement());
    }

    public ChessPiece withIsWhite(boolean isWhite) {
        if (isWhite == this.isWhite) return this;
        return new ChessPiece(pieceType, isWhite, hasMoved, ID_GENERATOR.getAndIncrement());
    }
}