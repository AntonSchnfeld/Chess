package de.schoenfeld.chess.core.ai;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.Move;

public interface MoveSearchStrategy {
    Move searchMove(GameState gameState);
}