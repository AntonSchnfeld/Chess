package de.schoenfeld.chess.rules.restrictive;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.MoveCollection;

@FunctionalInterface
public interface RestrictiveMoveRule<T extends PieceType> {
    void filterMoves(MoveCollection<T> moves, GameState<T> gameState);
}
