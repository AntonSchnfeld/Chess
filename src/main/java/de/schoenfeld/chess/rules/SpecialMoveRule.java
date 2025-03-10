package de.schoenfeld.chess.rules;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.MoveCollection;

public interface SpecialMoveRule {
    void apply(MoveCollection moves, GameState gameState);
}
