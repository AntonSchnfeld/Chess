package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.MoveCollection;

public class BishopMoveRule extends SlidingPieceMoveRule<StandardPieceType> {

    public BishopMoveRule() {
        super(SlidingPieceMoveRule.DIAGONAL_DIRECTIONS);
    }

    @Override
    public MoveCollection generateMoves(GameState<StandardPieceType> gameState) {
        var board = gameState.chessBoard();
        var bishops = board.getPiecesOfTypeAndColour(StandardPieceType.BISHOP,
                gameState.isWhiteTurn());
        var moves = new MoveCollection();

        for (var bishop : bishops) {
            generateMoves(gameState, bishop, moves);
        }

        return moves;
    }
}
