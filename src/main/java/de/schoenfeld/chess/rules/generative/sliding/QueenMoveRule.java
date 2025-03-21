package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.MoveCollection;

public class QueenMoveRule extends SlidingPieceMoveRule<StandardPieceType> {

    public QueenMoveRule() {
        super(SlidingPieceMoveRule.ALL_DIRECTIONS);
    }

    @Override
    public MoveCollection generateMoves(GameState<StandardPieceType> gameState) {
        var queens = gameState.chessBoard()
                .getPiecesOfTypeAndColour(StandardPieceType.QUEEN, gameState.isWhiteTurn());
        var moves = new MoveCollection();

        for (var queen : queens) {
            generateMoves(gameState, queen, moves);
        }

        return moves;
    }
}
