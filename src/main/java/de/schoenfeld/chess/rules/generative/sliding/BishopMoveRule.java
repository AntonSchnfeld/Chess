package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.StandardPieceType;

public class BishopMoveRule<T extends PieceType> extends SlidingPieceMoveRule<T> {
    private static final BishopMoveRule<StandardPieceType> STANDARD =
            new BishopMoveRule<>(StandardPieceType.BISHOP);

    public BishopMoveRule(T bishopType) {
        super(bishopType, SlidingPieceMoveRule.DIAGONAL_DIRECTIONS);
    }

    public static BishopMoveRule<StandardPieceType> standard() {
        return STANDARD;
    }
}
