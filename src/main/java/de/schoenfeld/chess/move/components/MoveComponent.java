package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.Move;

import java.io.Serializable;

public interface MoveComponent<T extends PieceType> extends Serializable {
    void executeOn(GameState<T> gameState, Move<T> move);

    void undoOn(GameState<T> gameState, Move<T> move);
}
