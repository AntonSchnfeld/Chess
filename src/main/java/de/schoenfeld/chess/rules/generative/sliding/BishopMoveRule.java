package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.MoveCollection;

import java.util.List;

public class BishopMoveRule extends SlidingPieceMoveRule<StandardPieceType> {

    public BishopMoveRule() {
        super(SlidingPieceMoveRule.DIAGONAL_DIRECTIONS);
    }

    @Override
    public MoveCollection<StandardPieceType> generateMoves(GameState<StandardPieceType> gameState) {
        List<Square> bishopSquares = gameState
                .getSquaresWithTypeAndColour(StandardPieceType.BISHOP,
                gameState.isWhiteTurn());
        MoveCollection<StandardPieceType> moves = new MoveCollection<>();

        for (Square square : bishopSquares) {
            generateMoves(gameState, square, moves);
        }

        return moves;
    }
}
