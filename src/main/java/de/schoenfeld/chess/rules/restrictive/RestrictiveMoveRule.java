package de.schoenfeld.chess.rules.restrictive;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.MoveCollection;

/**
 * A functional interface that filters a collection of moves based on some rule.
 */
@FunctionalInterface
public interface RestrictiveMoveRule {
    /**
     * Filters a collection of moves based on some rule.
     *
     * @param moves     The moves to filter
     * @param gameState The current game state
     */
    void filterMoves(MoveCollection moves, GameState gameState);
}
