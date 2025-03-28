package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;

import java.util.function.ToIntFunction;

public interface MoveOrderingHeuristic<T extends PieceType> extends ToIntFunction<Move<T>> {
}
