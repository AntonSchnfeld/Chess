package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.GameState;

import java.util.Optional;

public class NoPiecesRule implements GameConclusionRule {
    @Override
    public Optional<GameConclusion> detectGameEndCause(GameState gameState) {
        if (gameState.chessBoard().getPiecesOfColour(gameState.isWhiteTurn()).isEmpty()) {
            return Optional.of(
                    new GameConclusion(GameConclusion.Winner.of(!gameState.isWhiteTurn()),
                            "No pieces left for the other player")
            );
        }
        return Optional.empty();
    }
}
