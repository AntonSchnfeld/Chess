package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.MoveCollection;

public class QueenMoveRule extends SlidingPieceMoveRule {

    public QueenMoveRule() {
        super(SlidingPieceMoveRule.ALL_DIRECTIONS);
    }

    @Override
    public MoveCollection generateMoves(GameState gameState) {
        var queens = gameState.chessBoard().getPiecesOfTypeAndColour(PieceType.QUEEN, gameState.isWhiteTurn());
        var moves = new MoveCollection();

        for (var queen : queens) {
            generateMoves(gameState, queen, moves);
        }

        return moves;
    }
}
