package de.schoenfeld.chesskit.rules.restrictive;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveLookup;

@FunctionalInterface
public interface RestrictiveMoveRule<T extends Tile, P extends PieceType> {
    void filterMoves(MoveLookup<T, P> moves, GameState<T, P> gameState);
}
