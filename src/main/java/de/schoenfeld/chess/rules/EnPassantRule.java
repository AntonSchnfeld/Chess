package de.schoenfeld.chess.rules;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;

import java.util.List;

public class EnPassantRule implements SpecialMoveRule {
    @Override
    public void apply(MoveCollection moves, GameState gameState) {
        Move lastMove = gameState.moveHistory().getLastMove();

        // No last move or not pawn move
        if (lastMove == null ||
                !lastMove.movedPiece().getPieceType().equals(PieceType.PAWN)) {
            return;
        }

        // Not double advance
        if (Math.abs(lastMove.to().y() - lastMove.from().y()) != 2) {
            return;
        }

        int direction = (gameState.isWhiteTurn()) ? 1 : -1;

        Position enPassantTarget = new Position(lastMove.to().x(),
                lastMove.to().y() - direction);

        // Get pawns of opposite color that could capture
        List<ChessPiece> potentialCapturers = gameState
                .chessBoard()
                .getPiecesOfType(PieceType.PAWN, !lastMove.movedPiece().isWhite());

        for (ChessPiece pawn : potentialCapturers) {
            Position pawnPos = gameState.chessBoard().getPiecePosition(pawn);

            // Pawn must be adjacent to the moving pawn
            if (Math.abs(pawnPos.x() - lastMove.to().x()) == 1 && pawnPos.y() == lastMove.to().y()) {
                // Create an en passant move (pawn captures on enPassantTarget)
                moves.add(Move.of(pawn, pawnPos, enPassantTarget, new CaptureComponent(lastMove.movedPiece())));
            }
        }
    }
}
