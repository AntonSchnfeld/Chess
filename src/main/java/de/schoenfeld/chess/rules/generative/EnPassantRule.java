package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;

public class EnPassantRule implements GenerativeMoveRule {
    @Override
    public MoveCollection generateMoves(GameState gameState) {
        var board = gameState.chessBoard();
        var history = gameState.moveHistory();

        // check if there are any moves
        if (history.getMoveCount() == 0) return new MoveCollection();

        // check if the last move was a pawn move
        var lastMove = history.getLastMove();
        if (!PieceType.PAWN.equals(lastMove.movedPiece().pieceType()))
            return new MoveCollection();

        // check if the last move was a double pawn move
        if (Math.abs(lastMove.from().y() - lastMove.to().y()) != 2)
            return new MoveCollection();

        int direction = lastMove.movedPiece().isWhite() ? 1 : -1;
        Square enPassantTarget = Square.of(lastMove.to().x(), lastMove.to().y() + direction);
        MoveCollection moves = new MoveCollection();

        // Find the pawn that can capture the en passant target
        var enPassantPawn = board.getPieceAt(enPassantTarget.offset(1, -direction));
        // Check if the pawn is not null and is of the opposite color
        if (enPassantPawn != null
                && enPassantPawn.isWhite() != lastMove.movedPiece().isWhite()
                && enPassantPawn.pieceType().equals(PieceType.PAWN)) {
            moves.add(Move.of(
                    enPassantPawn,
                    board.getPiecePosition(enPassantPawn),
                    enPassantTarget,
                    new CaptureComponent(enPassantPawn)
            ));
        }

        enPassantPawn = board.getPieceAt(enPassantTarget.offset(-1, -direction));
        if (enPassantPawn != null
                && enPassantPawn.isWhite() != lastMove.movedPiece().isWhite()
                && enPassantPawn.pieceType().equals(PieceType.PAWN)) {
            moves.add(Move.of(
                    enPassantPawn,
                    board.getPiecePosition(enPassantPawn),
                    enPassantTarget,
                    new CaptureComponent(enPassantPawn)
            ));
        }

        return moves;
    }
}
