package de.schoenfeld.chesskit.rules.restrictive;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveCollection;

@FunctionalInterface
public interface RestrictiveMoveRule<T extends PieceType> {
    void filterMoves(MoveCollection<T> moves, GameState<T> gameState);
}
