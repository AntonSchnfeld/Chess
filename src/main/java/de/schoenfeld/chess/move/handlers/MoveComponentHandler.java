package de.schoenfeld.chess.move.handlers;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.components.MoveComponent;

public interface MoveComponentHandler<T extends MoveComponent> {
    GameState handle(GameState gameState, T component, Move move);
}
