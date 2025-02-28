package de.schoenfeld.chess.logic;

import de.schoenfeld.chess.data.ReadOnlyChessBoard;
import de.schoenfeld.chess.data.ReadOnlyGameState;
import de.schoenfeld.chess.data.Position;
import de.schoenfeld.chess.data.move.Move;
import de.schoenfeld.chess.data.move.MoveCollection;
import de.schoenfeld.chess.logic.piece.ChessPiece;
import de.schoenfeld.chess.logic.piece.PieceType;

public class CheckDetector {

    /**
     * Checks whether a certain move would result
     * in the King of the opposite colour being in check.
     * @param gameState The current game state
     * @param move The move to check
     * @return true if the move would result in the King being in check
     */
    public boolean isCheck(ReadOnlyGameState gameState, Move move) {
        ReadOnlyChessBoard board = gameState.getChessBoard();
        boolean isWhiteTurn = gameState.isWhiteTurn();
        boolean movingPlayerIsWhite = move.movedPiece().isWhite();

        // Determine the opponent's king position
        ChessPiece opponentKing = board.getPiecesOfType(PieceType.KING, !movingPlayerIsWhite)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No opponent king found"));
        Position kingPosition = board.getPiecePosition(opponentKing);

        // Check if the move puts the opponent's king in check
        MoveCollection pseudoLegalMoves = move.movedPiece()
                .getPieceType()
                .moveStrategy()
                .getPseudoLegalMoves(board, move.to());

        if (pseudoLegalMoves.containsMoveTo(kingPosition)) {
            return true;
        }

        // If the moving piece is a king, check if it moves into an attacked square
        if (move.movedPiece().getPieceType() == PieceType.KING) {
            return isSquareAttacked(board, move.to(), movingPlayerIsWhite);
        }

        return false;
    }

    /**
     * Determines if a given square is attacked by any opposing pieces.
     * @param board The current board state
     * @param square The square to check
     * @param isWhite The color of the player being checked
     * @return true if the square is attacked
     */
    private boolean isSquareAttacked(ReadOnlyChessBoard board, Position square, boolean isWhite) {
        return board.getPieces(isWhite)
                .stream()
                .flatMap(piece -> piece.getPieceType()
                        .moveStrategy()
                        .getPseudoLegalMoves(board, board.getPiecePosition(piece))
                        .stream())
                .anyMatch(move -> move.to().equals(square));
    }
}
