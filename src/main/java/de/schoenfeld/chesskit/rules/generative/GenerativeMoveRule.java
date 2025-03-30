package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveLookup;

@FunctionalInterface
public interface GenerativeMoveRule<T extends PieceType> {
    MoveLookup<T> generateMoves(GameState<T> gameState);
}
