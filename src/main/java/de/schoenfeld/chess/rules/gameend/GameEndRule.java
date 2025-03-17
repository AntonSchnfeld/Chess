package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.GameState;

import java.util.Optional;

public interface GameEndRule {
    Optional<GameConclusion> detectGameEndCause(GameState gameState);
}
