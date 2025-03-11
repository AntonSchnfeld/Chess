package de.schoenfeld.chess.rules;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;

public class CheckDetector {

    /**
     * Checks whether a certain move would result
     * in the King of the opposite colour being in check.
     * @param gameState The current game state
     * @param move The move to check
     * @return true if the move would result in the King being in check
     */
    public boolean isCheck(GameState gameState, Move move) {
        ImmutableChessBoard board = gameState.chessBoard();
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
    private boolean isSquareAttacked(ImmutableChessBoard board, Position square, boolean isWhite) {
        return board.getPiecesOfColour(isWhite)
                .stream()
                .flatMap(piece -> piece.getPieceType()
                        .moveStrategy()
                        .getPseudoLegalMoves(board, board.getPiecePosition(piece))
                        .stream())
                .anyMatch(move -> move.to().equals(square));
    }
}
