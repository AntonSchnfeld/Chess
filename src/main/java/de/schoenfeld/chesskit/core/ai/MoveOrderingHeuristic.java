package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;

import java.util.function.ToIntFunction;

public interface MoveOrderingHeuristic<T extends Tile, P extends PieceType> extends ToIntFunction<Move<T, P>> {
}
