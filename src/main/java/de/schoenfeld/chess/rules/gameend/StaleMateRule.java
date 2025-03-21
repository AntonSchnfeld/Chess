package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.MoveGenerator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StaleMateRule implements GameConclusionRule {
    private final MoveGenerator moveGenerator;

    public StaleMateRule(MoveGenerator moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    @Override
    public Optional<GameConclusion> detectGameEndCause(GameState gameState) {
        // If the current player has no legal moves and is NOT in check, it's a stalemate
        if (moveGenerator.generateMoves(gameState).isEmpty() && allKingsSafe(gameState))
            return Optional.of(
                    new GameConclusion(GameConclusion.Winner.NONE, "Stalemate")
            );
        return Optional.empty();
    }

    private boolean allKingsSafe(GameState gameState) {
        ChessBoard board = gameState.chessBoard();
        boolean isWhiteTurn = gameState.isWhiteTurn();
        // Get all kings
        List<ChessPiece> kings = board.getPiecesOfTypeAndColour(PieceType.KING, isWhiteTurn);
        if (kings.isEmpty()) return true; // No kings => no check
        // withTurnSwitched to generate moves for the opposite player
        MoveCollection opponentMoves = moveGenerator.generateMoves(gameState.withTurnSwitched());

        for (ChessPiece king : kings) {
            // Get king pos and check if opponent has any move to that square
            Square kingPos = board.getPiecePosition(king);
            if (opponentMoves.containsMoveTo(kingPos)) return false;
        }
        return true; // No check found, everyone is happy
    }
}
