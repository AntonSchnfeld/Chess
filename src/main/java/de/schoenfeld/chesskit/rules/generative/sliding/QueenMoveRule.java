package de.schoenfeld.chesskit.rules.generative.sliding;

import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.StandardPieceType;

public class QueenMoveRule<P extends PieceType> extends SlidingPieceMoveRule<P> {
    private static final QueenMoveRule<StandardPieceType> STANDARD =
            new QueenMoveRule<>(StandardPieceType.QUEEN);

    public QueenMoveRule(P queenType) {
        super(queenType, SlidingPieceMoveRule.ALL_DIRECTIONS);
    }

    public static QueenMoveRule<StandardPieceType> standard() {
        return STANDARD;
    }
}
