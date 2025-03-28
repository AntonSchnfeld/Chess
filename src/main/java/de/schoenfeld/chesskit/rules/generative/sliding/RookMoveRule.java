package de.schoenfeld.chesskit.rules.generative.sliding;

import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.StandardPieceType;

public class RookMoveRule<T extends PieceType> extends SlidingPieceMoveRule<T> {
    private final static RookMoveRule<StandardPieceType> STANDARD =
            new RookMoveRule<>(StandardPieceType.ROOK);

    public RookMoveRule(T rookType) {
        super(rookType, SlidingPieceMoveRule.STRAIGHT_DIRECTIONS);
    }

    public static RookMoveRule<StandardPieceType> standard() {
        return STANDARD;
    }
}
