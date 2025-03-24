package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.MoveGenerator;

import java.util.List;
import java.util.Optional;

public class StaleMateRule implements GameConclusionRule<StandardPieceType> {
    private final MoveGenerator<StandardPieceType> moveGenerator;

    public StaleMateRule(MoveGenerator<StandardPieceType> moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    @Override
    public Optional<GameConclusion> detectGameEndCause(GameState<StandardPieceType> gameState) {
        // If the current player has no legal moves and is NOT in check, it's a stalemate
        if (moveGenerator.generateMoves(gameState).isEmpty() && allKingsSafe(gameState))
            return Optional.of(
                    new GameConclusion(GameConclusion.Winner.NONE, "Stalemate")
            );
        return Optional.empty();
    }

    private boolean allKingsSafe(GameState<StandardPieceType> gameState) {
        ChessBoard<StandardPieceType> board = gameState.chessBoard();
        boolean isWhiteTurn = gameState.isWhiteTurn();
        // Get all kings
        List<Square> kingSquares = board
                .getSquaresWithTypeAndColour(StandardPieceType.KING, isWhiteTurn);
        if (kingSquares.isEmpty()) return true; // No kings => no check
        // withTurnSwitched to generate moves for the opposite player
        MoveCollection<StandardPieceType> opponentMoves = moveGenerator
                .generateMoves(gameState.withTurnSwitched());

        for (Square square : kingSquares) {
            // Get king pos and check if opponent has any move to that square
            if (opponentMoves.containsMoveTo(square)) return false;
        }
        return true; // No check found, everyone is happy
    }
}
