package de.schoenfeld.chess.rules;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.MoveCollection;

public class CastlingRule implements SpecialMoveRule {
    @Override
    public void apply(MoveCollection moves, GameState gameState) {
        // TODO: Implement this method
        // Should detect whenever castling is avaible
        // If it is possible, add a new Move with a CastlingComponent

    }
}
