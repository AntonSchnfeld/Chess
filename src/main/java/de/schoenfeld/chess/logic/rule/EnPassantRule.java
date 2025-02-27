package de.schoenfeld.chess.logic.rule;

import de.schoenfeld.chess.Position;
import de.schoenfeld.chess.data.ReadOnlyGameState;
import de.schoenfeld.chess.data.move.CaptureComponent;
import de.schoenfeld.chess.data.move.Move;
import de.schoenfeld.chess.data.move.MoveCollection;
import de.schoenfeld.chess.logic.piece.ChessPiece;
import de.schoenfeld.chess.logic.piece.PieceType;

import java.util.List;

public class EnPassantRule implements MoveRule {
    @Override
    public void apply(MoveCollection moves, ReadOnlyGameState gameState) {
        Move lastMove = gameState.getMoveHistory().getLastMove();

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
                .getChessBoard()
                .getPiecesOfType(PieceType.PAWN, !lastMove.movedPiece().isWhite());

        for (ChessPiece pawn : potentialCapturers) {
            Position pawnPos = gameState.getChessBoard().getPiecePosition(pawn);

            // Pawn must be adjacent to the moving pawn
            if (Math.abs(pawnPos.x() - lastMove.to().x()) == 1 && pawnPos.y() == lastMove.to().y()) {
                // Create an en passant move (pawn captures on enPassantTarget)
                moves.add(Move.of(pawn, pawnPos, enPassantTarget, new CaptureComponent(lastMove.movedPiece())));
            }
        }
    }
}
