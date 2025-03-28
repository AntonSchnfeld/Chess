package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;

public interface MoveSearchStrategy<T extends PieceType> {
    Move<T> searchMove(GameState<T> gameState);
}