package de.schoenfeld.chess.rules;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.MoveCollection;

public interface MoveGenerator {
    MoveCollection generateMoves(GameState gameState);
}
