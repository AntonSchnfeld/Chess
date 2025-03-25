package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.StandardPieceType;

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
