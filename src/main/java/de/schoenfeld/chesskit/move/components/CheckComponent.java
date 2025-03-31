package de.schoenfeld.chesskit.move.components;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;

public record CheckComponent<T extends PieceType>() implements MoveComponent<T> {
    @Override
    public void makeOn(GameState<T> gameState, Move<T> move) {

    }

    @Override
    public void unmakeOn(GameState<T> gameState, Move<T> move) {

    }
}
