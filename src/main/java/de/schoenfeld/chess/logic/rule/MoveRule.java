package de.schoenfeld.chess.logic.rule;

import de.schoenfeld.chess.data.ReadOnlyGameState;
import de.schoenfeld.chess.data.move.MoveCollection;

public interface MoveRule {
    void apply(MoveCollection moves, ReadOnlyGameState gameState);
}
