package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.GameState;

import java.util.Optional;

public interface GameConclusionRule {
    Optional<GameConclusion> detectGameEndCause(GameState gameState);
}
