package de.schoenfeld.chesskit.rules.gameend;

import de.schoenfeld.chesskit.events.GameConclusion;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;

import java.util.Optional;

public class NoPiecesRule<T extends PieceType> implements GameConclusionRule<T> {
    @Override
    public Optional<GameConclusion> detectGameEndCause(GameState<T> gameState) {
        if (gameState.getChessBoard().getSquaresWithColour(gameState.isWhiteTurn()).isEmpty()) {
            return Optional.of(
                    new GameConclusion(GameConclusion.Winner.of(!gameState.isWhiteTurn()),
                            "No pieces left for the other player")
            );
        }
        return Optional.empty();
    }
}
