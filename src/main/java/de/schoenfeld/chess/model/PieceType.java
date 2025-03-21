package de.schoenfeld.chess.model;

import java.io.Serializable;

public record PieceType(int value, String symbol) implements Serializable {
    public static final PieceType PAWN = new PieceType(100, "");
    public static final PieceType KING = new PieceType(0, "K");
    public static final PieceType BISHOP = new PieceType(330, "B");
    public static final PieceType KNIGHT = new PieceType(320, "N");
    public static final PieceType ROOK = new PieceType(500, "R");
    public static final PieceType QUEEN = new PieceType(900, "Q");
}
