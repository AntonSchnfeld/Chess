package de.schoenfeld.chess.core.ai;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;

@FunctionalInterface
public interface GameStateEvaluator<T extends PieceType> {
    int evaluate(GameState<T> gameState);
}
