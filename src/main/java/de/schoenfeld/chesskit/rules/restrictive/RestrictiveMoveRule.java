package de.schoenfeld.chesskit.rules.restrictive;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveLookup;

@FunctionalInterface
public interface RestrictiveMoveRule<T extends PieceType> {
    void filterMoves(MoveLookup<T> moves, GameState<T> gameState);
}
