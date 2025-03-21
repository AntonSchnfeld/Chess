package de.schoenfeld.chess.rules;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.MoveCollection;

@FunctionalInterface
public interface MoveGenerator<T extends PieceType> {
    MoveCollection<T> generateMoves(GameState<T> gameState);
}
