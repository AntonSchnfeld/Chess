package de.schoenfeld.chesskit.rules.gameend;

import de.schoenfeld.chesskit.events.GameConclusion;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;

import java.util.Optional;

public interface GameConclusionRule<T extends PieceType> {
    Optional<GameConclusion> detectGameEndCause(GameState<T> gameState);
}
