package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;

import java.util.Optional;

public class InsufficientMaterialRule implements GameConclusionRule {
    @Override
    public Optional<GameConclusion> detectGameEndCause(GameState gameState) {
        ChessBoard board = gameState.chessBoard();

        if (!board.getPiecesOfType(PieceType.PAWN).isEmpty() ||
                !board.getPiecesOfType(PieceType.ROOK).isEmpty() ||
                !board.getPiecesOfType(PieceType.QUEEN).isEmpty() ||
                (!board.getPiecesOfType(PieceType.KNIGHT).isEmpty() &&
                        !board.getPiecesOfType(PieceType.BISHOP).isEmpty()) ||
                board.getPiecesOfType(PieceType.KNIGHT).size() > 1 ||
                board.getPiecesOfType(PieceType.BISHOP).size() > 1)
            return Optional.empty();

        return Optional.of(new GameConclusion(GameConclusion.Winner.NONE, "Insufficient material"));
    }
}
