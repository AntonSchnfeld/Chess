package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.rules.MoveGenerator;

import java.util.Optional;

public class CheckMateRule implements GameEndRule {
    private final MoveGenerator moveGenerator;

    public CheckMateRule(MoveGenerator moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    @Override
    public Optional<GameConclusion> detectGameEndCause(GameState gameState) {
        var moves = moveGenerator.generateMoves(gameState);

        // If the current player has no legal moves, check if their king is attacked
        if (moves.isEmpty() && isKingAttacked(gameState)) {
            return Optional.of(new GameConclusion(
                    gameState.isWhiteTurn() ? GameConclusion.Winner.BLACK
                            : GameConclusion.Winner.WHITE,
                    "Checkmate"
            ));
        }

        return Optional.empty();
    }

    private boolean isKingAttacked(GameState gameState) {
        var board = gameState.chessBoard();
        boolean isWhiteTurn = gameState.isWhiteTurn();

        // Locate the king
        ChessPiece king = board.getPiecesOfType(PieceType.KING, isWhiteTurn)
                .stream()
                .findFirst()
                .orElse(null);
        if (king == null) return false; // Should never happen unless the game is corrupted

        var enemyState = gameState.withIsWhiteTurn(!gameState.isWhiteTurn());
        var opponentMoves = moveGenerator.generateMoves(enemyState);

        // Check if any move targets the king's position
        return opponentMoves
                .stream()
                .anyMatch(move -> move.to().equals(board.getPiecePosition(king)));
    }
}
