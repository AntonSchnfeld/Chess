package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.model.*;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveCollection;
import de.schoenfeld.chesskit.move.components.EnPassantComponent;

public class EnPassantRule implements GenerativeMoveRule<StandardPieceType> {
    @Override
    public MoveCollection<StandardPieceType> generateMoves(GameState<StandardPieceType> gameState) {
        MoveHistory<StandardPieceType> history = gameState.getMoveHistory();

        // Ensure that there is at least one move
        if (history.getMoveCount() == 0) return new MoveCollection<>();

        // Retrieve the last move
        Move<StandardPieceType> lastMove = history.getLastMove();
        ChessPiece<StandardPieceType> lastMovedPiece = gameState.getPieceAt(lastMove.to());

        // Ensure the last move was a pawn moving exactly two squares forward
        if (lastMovedPiece == null
                || !lastMovedPiece.pieceType().equals(StandardPieceType.PAWN)
                || Math.abs(lastMove.from().y() - lastMove.to().y()) != 2) {
            return new MoveCollection<>();
        }

        // The en passant capture square (where the capturing pawn lands)
        int enPassantRow = lastMove.from().y() + (lastMovedPiece.isWhite() ? 1 : -1);
        Square enPassantTarget = Square.of(lastMove.to().x(), enPassantRow);

        MoveCollection<StandardPieceType> moves = new MoveCollection<>();

        // The current player's color (only their pawns can capture en passant)
        boolean currentTurnIsWhite = gameState.isWhiteTurn();

        // Check left adjacent square for an eligible capturing pawn
        Square leftPawnPos = lastMove.to().offset(-1, 0);
        checkAndAddEnPassantMove(gameState, lastMove.to(), enPassantTarget, moves, leftPawnPos, currentTurnIsWhite);

        // Check right adjacent square for an eligible capturing pawn
        Square rightPawnPos = lastMove.to().offset(1, 0);
        checkAndAddEnPassantMove(gameState, lastMove.to(), enPassantTarget, moves, rightPawnPos, currentTurnIsWhite);

        return moves;
    }

    private void checkAndAddEnPassantMove(
            GameState<StandardPieceType> gameState,
            Square capturedPawnSquare,
            Square enPassantTarget,
            MoveCollection<StandardPieceType> moves,
            Square adjacentPawnPos,
            boolean currentTurnIsWhite) {

        if (!gameState.getBounds().contains(adjacentPawnPos)) return;

        ChessPiece<StandardPieceType> adjacentPawn = gameState.getPieceAt(adjacentPawnPos);

        if (adjacentPawn != null
                && adjacentPawn.isWhite() == currentTurnIsWhite // The capturing pawn must be owned by the current player
                && adjacentPawn.pieceType().equals(StandardPieceType.PAWN)) {
            moves.add(Move.of(
                    adjacentPawn,
                    adjacentPawnPos,
                    enPassantTarget,
                    new EnPassantComponent(gameState.getPieceAt(capturedPawnSquare), capturedPawnSquare)
            ));
        }
    }
}
