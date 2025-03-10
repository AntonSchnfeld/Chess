package de.schoenfeld.chess.model;

import de.schoenfeld.chess.move.strategy.*;

import java.io.Serializable;

public record PieceType(int value, String symbol, MoveStrategy moveStrategy) implements Serializable {
    public static final PieceType PAWN = new PieceType(1, "", new PawnMoveStrategy());
    public static final PieceType KING = new PieceType(0, "K", new KingMoveStrategy());
    public static final PieceType BISHOP = new PieceType(3, "B", new SlidingMoveStrategy(SlidingMoveStrategy.DIAGONAL_DIRECTIONS));
    public static final PieceType KNIGHT = new PieceType(3, "N", new KnightMoveStrategy());
    public static final PieceType ROOK = new PieceType(5, "R", new SlidingMoveStrategy(SlidingMoveStrategy.STRAIGHT_DIRECTIONS));
    public static final PieceType QUEEN = new PieceType(8, "Q", new SlidingMoveStrategy(SlidingMoveStrategy.ALL_DIRECTIONS));
}
