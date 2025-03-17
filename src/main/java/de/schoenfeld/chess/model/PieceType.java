package de.schoenfeld.chess.model;

import java.io.Serializable;

public record PieceType(int value, String symbol) implements Serializable {
    public static final PieceType PAWN = new PieceType(1, "");
    public static final PieceType KING = new PieceType(0, "K");
    public static final PieceType BISHOP = new PieceType(3, "B");
    public static final PieceType KNIGHT = new PieceType(3, "N");
    public static final PieceType ROOK = new PieceType(5, "R");
    public static final PieceType QUEEN = new PieceType(8, "Q");
}
