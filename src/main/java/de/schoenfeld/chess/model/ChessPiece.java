package de.schoenfeld.chess.model;

import java.io.Serializable;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;

public record ChessPiece<T extends PieceType>(
        T pieceType,
        boolean isWhite,
        boolean hasMoved,
        long uniqueId
) implements Serializable {
    private static final WeakHashMap<Long, ChessPiece<?>> PIECE_ID_MAP = new WeakHashMap<>(32);

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    public ChessPiece {
        synchronized (PIECE_ID_MAP) {
            PIECE_ID_MAP.put(uniqueId, this);
        }
    }

    public ChessPiece(T pieceType, boolean isWhite) {
        this(pieceType, isWhite, false, ID_GENERATOR.getAndIncrement());
    }

    public static ChessPiece<?> getById(long uniqueId) {
        synchronized (PIECE_ID_MAP) {
            return PIECE_ID_MAP.get(uniqueId);
        }
    }

    public ChessPiece<T> withMoved(boolean hasMoved) {
        if (hasMoved == this.hasMoved) return this;
        return new ChessPiece<>(pieceType, isWhite, hasMoved, ID_GENERATOR.getAndIncrement());
    }

    public ChessPiece<T> withIsWhite(boolean isWhite) {
        if (isWhite == this.isWhite) return this;
        return new ChessPiece<>(pieceType, isWhite, hasMoved, ID_GENERATOR.getAndIncrement());
    }
}