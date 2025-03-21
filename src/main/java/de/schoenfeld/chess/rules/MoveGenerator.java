package de.schoenfeld.chess.rules;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.MoveCollection;

/**
 * Generates all legal moves for a given game state.
 * <p>
 * Implementations of this interface are responsible for computing all possible moves
 * based on the rules of the specific chess variant being used.
 * </p>
 *
 * @author Anton Schönfeld
 */
public interface MoveGenerator {

    /**
     * Generates all possible moves for the given {@link GameState}.
     * <p>
     * The method will never return {@code null}. If no moves are possible,
     * an empty {@link MoveCollection} is returned. If {@code gameState} is {@code null},
     * a {@link NullPointerException} is thrown.
     * </p>
     *
     * @param gameState the current state of the game, must not be {@code null}
     * @return a {@link MoveCollection} containing all legal moves for the current player
     * @throws NullPointerException if {@code gameState} is {@code null}
     */
    MoveCollection generateMoves(GameState gameState);
}
