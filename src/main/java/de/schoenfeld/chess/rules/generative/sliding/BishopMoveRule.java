package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.MoveCollection;

public class BishopMoveRule extends SlidingPieceMoveRule {

    public BishopMoveRule() {
        super(SlidingPieceMoveRule.DIAGONAL_DIRECTIONS);
    }

    @Override
    public MoveCollection generateMoves(GameState gameState) {
        var board = gameState.chessBoard();
        var bishops = board.getPiecesOfTypeAndColour(PieceType.BISHOP, gameState.isWhiteTurn());
        var moves = new MoveCollection();

        for (var bishop : bishops) {
            generateMoves(gameState, bishop, moves);
        }

        return moves;
    }
}
