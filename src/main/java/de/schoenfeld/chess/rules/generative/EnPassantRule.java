package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.model.*;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;

public class EnPassantRule implements GenerativeMoveRule<StandardPieceType> {
    @Override
    public MoveCollection<StandardPieceType> generateMoves(GameState<StandardPieceType> gameState) {
        MoveHistory<StandardPieceType> history = gameState.moveHistory();

        // Check if there are any moves
        if (history.getMoveCount() == 0) return new MoveCollection<>();

        // Check if the last move was a pawn move
        Move<StandardPieceType> lastMove = history.getLastMove();
        if (!StandardPieceType.PAWN.equals(lastMove.movedPiece().pieceType()))
            return new MoveCollection<>();

        // Check if the last move was a double pawn move
        if (Math.abs(lastMove.from().y() - lastMove.to().y()) != 2)
            return new MoveCollection<>();

        // Calculate en passant target properly:
        int enPassantRow = lastMove.movedPiece().isWhite() ? lastMove.to().y() - 1 : lastMove.to().y() + 1;
        Square enPassantTarget = Square.of(lastMove.to().x(), enPassantRow);

        MoveCollection<StandardPieceType> moves = new MoveCollection<>();

        // Check left adjacent square
        Square leftPawnPos = enPassantTarget.offset(-1, 0);
        ChessPiece<StandardPieceType> leftPawn = gameState.getPieceAt(leftPawnPos);
        if (leftPawn != null
                && leftPawn.isWhite() != lastMove.movedPiece().isWhite()
                && leftPawn.pieceType().equals(StandardPieceType.PAWN)) {
            moves.add(Move.of(
                    leftPawn,
                    leftPawnPos,
                    enPassantTarget,
                    new CaptureComponent<>(lastMove.movedPiece()) // Capturing the pawn that moved two squares
            ));
        }

        // Check right adjacent square
        Square rightPawnPos = enPassantTarget.offset(1, 0);
        ChessPiece<StandardPieceType> rightPawn = gameState.getPieceAt(rightPawnPos);
        if (rightPawn != null
                && rightPawn.isWhite() != lastMove.movedPiece().isWhite()
                && rightPawn.pieceType().equals(StandardPieceType.PAWN)) {
            moves.add(Move.of(
                    rightPawn,
                    rightPawnPos,
                    enPassantTarget,
                    new CaptureComponent<>(lastMove.movedPiece())
            ));
        }

        return moves;
    }

}
