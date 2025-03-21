package de.schoenfeld.chess.core.ai;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.Move;

public interface MoveSearchStrategy<T extends PieceType> {
    Move<T> searchMove(GameState<T> gameState);
}