package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.StandardPieceType;

import java.util.Optional;

public class InsufficientMaterialRule implements GameConclusionRule<StandardPieceType> {
    @Override
    public Optional<GameConclusion> detectGameEndCause(GameState<StandardPieceType> gameState) {
        ChessBoard<StandardPieceType> board = gameState.chessBoard();

        if (!board.getPiecesOfType(StandardPieceType.PAWN).isEmpty() ||
                !board.getPiecesOfType(StandardPieceType.ROOK).isEmpty() ||
                !board.getPiecesOfType(StandardPieceType.QUEEN).isEmpty() ||
                (!board.getPiecesOfType(StandardPieceType.KNIGHT).isEmpty() &&
                        !board.getPiecesOfType(StandardPieceType.BISHOP).isEmpty()) ||
                board.getPiecesOfType(StandardPieceType.KNIGHT).size() > 1 ||
                board.getPiecesOfType(StandardPieceType.BISHOP).size() > 1)
            return Optional.empty();

        return Optional.of(
                new GameConclusion(GameConclusion.Winner.NONE,
                        "Insufficient material")
        );
    }
}
