package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;

@FunctionalInterface
public interface GameStateEvaluator<T extends PieceType> {
    int evaluate(GameState<T> gameState);
}
