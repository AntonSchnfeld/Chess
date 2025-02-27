package de.schoenfeld.chess.logic.componenthandler;

import de.schoenfeld.chess.data.GameState;
import de.schoenfeld.chess.data.move.Move;
import de.schoenfeld.chess.data.move.MoveComponent;

public interface MoveComponentHandler<T extends MoveComponent> {
    void handle(GameState gameState, T component, Move move);
}
