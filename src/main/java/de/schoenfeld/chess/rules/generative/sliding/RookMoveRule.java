package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.MoveCollection;

import java.util.List;

public class RookMoveRule extends SlidingPieceMoveRule<StandardPieceType> {

    public RookMoveRule() {
        super(SlidingPieceMoveRule.STRAIGHT_DIRECTIONS);
    }

    @Override
    public MoveCollection<StandardPieceType> generateMoves(GameState<StandardPieceType> gameState) {
        List<Square> rookSquares = gameState
                .getSquaresWithTypeAndColour(StandardPieceType.ROOK, gameState.isWhiteTurn());
        MoveCollection<StandardPieceType> moves = new MoveCollection<>();

        for (Square square : rookSquares) {
            generateMoves(gameState, square, moves);
        }

        return moves;
    }
}
