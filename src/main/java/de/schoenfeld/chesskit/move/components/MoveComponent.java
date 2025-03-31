package de.schoenfeld.chesskit.move.components;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;

import java.io.Serializable;

public interface MoveComponent<T extends PieceType> extends Serializable {
    void makeOn(GameState<T> gameState, Move<T> move);

    void unmakeOn(GameState<T> gameState, Move<T> move);
}
