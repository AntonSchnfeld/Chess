package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveCollection;

@FunctionalInterface
public interface GenerativeMoveRule<T extends PieceType> {
    MoveCollection<T> generateMoves(GameState<T> gameState);
}
