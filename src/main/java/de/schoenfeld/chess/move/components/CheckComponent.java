package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.Move;

public record CheckComponent<T extends PieceType>() implements MoveComponent<T> {
    @Override
    public void executeOn(GameState<T> gameState, Move<T> move) {

    }

    @Override
    public void undoOn(GameState<T> gameState, Move<T> move) {

    }
}
