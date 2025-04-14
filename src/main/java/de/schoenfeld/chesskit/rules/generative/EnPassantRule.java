package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.*;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.EnPassantComponent;

public class EnPassantRule<P extends PieceType> implements GenerativeMoveRule<Square8x8, P> {
    private final P pawnType;

    public EnPassantRule(P pawnType) {
        this.pawnType = pawnType;
    }

    private final static EnPassantRule<StandardPieceType> STANDARD =
            new EnPassantRule<>(StandardPieceType.PAWN);

    public static EnPassantRule<StandardPieceType> standard() {
        return STANDARD;
    }

    @Override
    public void generateMoves(GameState<Square8x8, P> gameState,
                              MoveLookup<Square8x8, P> moves) {
        MoveHistory<Square8x8, P> history = gameState.getMoveHistory();

        // Ensure that there's at least one move
        if (history.getMoveCount() == 0) return;

        // Retrieve the last move
        Move<Square8x8, P> lastMove = history.getLastMove();
        ChessPiece<P> lastMovedPiece = gameState.getPieceAt(lastMove.to());

        // Ensure the last move was a pawn moving exactly two squares forward
        if (lastMovedPiece == null
                || !lastMovedPiece.pieceType().equals(pawnType)
                || Math.abs(lastMove.from().y() - lastMove.to().y()) != 2) {
            return;
        }

        // The en passant capture square (where the capturing pawn lands)
        int enPassantRow = lastMove.from().y() + (lastMovedPiece.color() == Color.WHITE ? 1 : -1);
        Square8x8 enPassantTarget = Square8x8.of(lastMove.to().x(), enPassantRow);

        // The current player's color (only their pawns can capture en passant)
        Color currentTurn = gameState.getColor();

        // Check left adjacent square for an eligible capturing pawn
        Square8x8 leftPawnPos = lastMove.to().offset(-1, 0);
        checkAndAddEnPassantMove(gameState, lastMove.to(), enPassantTarget, moves, leftPawnPos, currentTurn);

        // Check right adjacent square for an eligible capturing pawn
        Square8x8 rightPawnPos = lastMove.to().offset(1, 0);
        checkAndAddEnPassantMove(gameState, lastMove.to(), enPassantTarget, moves, rightPawnPos, currentTurn);
    }

    private void checkAndAddEnPassantMove(
            GameState<Square8x8, P> gameState,
            Square8x8 capturedPawnSquare8x8,
            Square8x8 enPassantTarget,
            MoveLookup<Square8x8, P> moves,
            Square8x8 adjacentPawnPos,
            Color currentTurn) {

        if (!gameState.getBounds().contains(adjacentPawnPos)) return;

        ChessPiece<P> adjacentPawn = gameState.getPieceAt(adjacentPawnPos);

        if (adjacentPawn != null
                && adjacentPawn.color() == currentTurn // The capturing pawn must be owned by the current player
                && adjacentPawn.pieceType().equals(pawnType)) {
            moves.add(Move.of(
                    adjacentPawn,
                    adjacentPawnPos,
                    enPassantTarget,
                    new EnPassantComponent<>(gameState.getPieceAt(capturedPawnSquare8x8), capturedPawnSquare8x8)
            ));
        }
    }
}
