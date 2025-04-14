package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;

public interface MoveSearchStrategy<T extends Tile, P extends PieceType> {
    Move<T, P> searchMove(GameState<T, P> gameState);
}