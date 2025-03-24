package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.MoveCollection;

import java.util.List;

public class QueenMoveRule extends SlidingPieceMoveRule<StandardPieceType> {

    public QueenMoveRule() {
        super(SlidingPieceMoveRule.ALL_DIRECTIONS);
    }

    @Override
    public MoveCollection<StandardPieceType> generateMoves(GameState<StandardPieceType> gameState) {
        List<Square> queenSquares = gameState.chessBoard()
                .getSquaresWithTypeAndColour(StandardPieceType.QUEEN, gameState.isWhiteTurn());
        MoveCollection<StandardPieceType> moves = new MoveCollection<>();

        for (Square square : queenSquares) {
            generateMoves(gameState, square, moves);
        }

        return moves;
    }
}
