package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.MoveCollection;

public class RookMoveRule extends SlidingPieceMoveRule {

    public RookMoveRule() {
        super(SlidingPieceMoveRule.STRAIGHT_DIRECTIONS);
    }

    @Override
    public MoveCollection generateMoves(GameState gameState) {
        var board = gameState.chessBoard();
        var rooks = board.getPiecesOfTypeAndColour(PieceType.ROOK, gameState.isWhiteTurn());
        var moves = new MoveCollection();

        for (var rook : rooks) {
            generateMoves(gameState, rook, moves);
        }

        return moves;
    }
}
