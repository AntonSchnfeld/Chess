package de.schoenfeld.chesskit.rules.generative.sliding;

import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.StandardPieceType;

public class QueenMoveRule<T extends PieceType> extends SlidingPieceMoveRule<T> {
    private static final QueenMoveRule<StandardPieceType> STANDARD =
            new QueenMoveRule<>(StandardPieceType.QUEEN);

    public QueenMoveRule(T queenType) {
        super(queenType, SlidingPieceMoveRule.ALL_DIRECTIONS);
    }

    public static QueenMoveRule<StandardPieceType> standard() {
        return STANDARD;
    }
}
