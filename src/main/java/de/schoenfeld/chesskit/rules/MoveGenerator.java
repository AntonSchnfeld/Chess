package de.schoenfeld.chesskit.rules;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveCollection;

@FunctionalInterface
public interface MoveGenerator<T extends PieceType> {
    MoveCollection<T> generateMoves(GameState<T> gameState);
}
