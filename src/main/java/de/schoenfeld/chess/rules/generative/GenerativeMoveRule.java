package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.MoveCollection;

/**
 * A functional interface that generates a collection of moves based on some rule.
 *
 * @author Anton Schoenfeld
 */
@FunctionalInterface
public interface GenerativeMoveRule {
    /**
     * Generates a collection of moves based on some rule.
     *
     * @param gameState The current game state
     * @return A {@link MoveCollection} containing the generated moves
     * @throws NullPointerException if {@code gameState} is null
     */
    MoveCollection generateMoves(GameState gameState);
}
