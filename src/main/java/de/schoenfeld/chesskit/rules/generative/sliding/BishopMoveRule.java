package de.schoenfeld.chesskit.rules.generative.sliding;

import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.StandardPieceType;

public class BishopMoveRule<P extends PieceType> extends SlidingPieceMoveRule<P> {
    private static final BishopMoveRule<StandardPieceType> STANDARD =
            new BishopMoveRule<>(StandardPieceType.BISHOP);

    public BishopMoveRule(P bishopType) {
        super(bishopType, SlidingPieceMoveRule.DIAGONAL_DIRECTIONS);
    }

    public static BishopMoveRule<StandardPieceType> standard() {
        return STANDARD;
    }
}
