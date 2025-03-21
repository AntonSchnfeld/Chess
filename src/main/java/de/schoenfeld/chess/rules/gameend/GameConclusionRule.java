package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;

import java.util.Optional;

public interface GameConclusionRule<T extends PieceType> {
    Optional<GameConclusion> detectGameEndCause(GameState<T> gameState);
}
