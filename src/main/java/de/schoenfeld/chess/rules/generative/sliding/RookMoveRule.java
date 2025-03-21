package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.MoveCollection;

import java.util.List;

public class RookMoveRule extends SlidingPieceMoveRule<StandardPieceType> {

    public RookMoveRule() {
        super(SlidingPieceMoveRule.STRAIGHT_DIRECTIONS);
    }

    @Override
    public MoveCollection<StandardPieceType> generateMoves(GameState<StandardPieceType> gameState) {
        ChessBoard<StandardPieceType> board = gameState.chessBoard();
        List<ChessPiece<StandardPieceType>> rooks = board
                .getPiecesOfTypeAndColour(StandardPieceType.ROOK, gameState.isWhiteTurn());
        MoveCollection<StandardPieceType> moves = new MoveCollection<>();

        for (ChessPiece<StandardPieceType> rook : rooks) {
            generateMoves(gameState, rook, moves);
        }

        return moves;
    }
}
