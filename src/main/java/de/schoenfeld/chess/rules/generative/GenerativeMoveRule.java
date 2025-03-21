package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.MoveCollection;

@FunctionalInterface
public interface GenerativeMoveRule {
    MoveCollection generateMoves(GameState gameState);
}
