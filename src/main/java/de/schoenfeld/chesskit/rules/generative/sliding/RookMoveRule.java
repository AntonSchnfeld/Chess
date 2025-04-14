package de.schoenfeld.chesskit.rules.generative.sliding;

import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.StandardPieceType;

public class RookMoveRule<P extends PieceType> extends SlidingPieceMoveRule<P> {
    private final static RookMoveRule<StandardPieceType> STANDARD =
            new RookMoveRule<>(StandardPieceType.ROOK);

    public RookMoveRule(P rookType) {
        super(rookType, SlidingPieceMoveRule.STRAIGHT_DIRECTIONS);
    }

    public static RookMoveRule<StandardPieceType> standard() {
        return STANDARD;
    }
}
