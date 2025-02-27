package de.schoenfeld.chess.logic;

import de.schoenfeld.chess.data.ReadOnlyChessBoard;
import de.schoenfeld.chess.data.ReadOnlyGameState;
import de.schoenfeld.chess.data.move.Move;
import de.schoenfeld.chess.logic.piece.PieceType;

public class CheckDetector {
    public boolean isCheck(ReadOnlyGameState gameState, Move move) {
        ReadOnlyChessBoard board = gameState.getChessBoard();

        return board.getPiecesOfType(PieceType.KING, !move.movedPiece().isWhite())
                .stream()
                .map(board::getPiecePosition)
                .anyMatch(kingPos -> kingPos.equals(move.to()));
    }
}
