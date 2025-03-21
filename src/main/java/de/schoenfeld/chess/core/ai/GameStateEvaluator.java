package de.schoenfeld.chess.core.ai;

import de.schoenfeld.chess.model.GameState;

@FunctionalInterface
public interface GameStateEvaluator {
    int evaluate(GameState gameState);
}
