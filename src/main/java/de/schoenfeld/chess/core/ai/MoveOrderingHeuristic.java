package de.schoenfeld.chess.core.ai;

import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.Move;

import java.util.function.ToIntFunction;

public interface MoveOrderingHeuristic<T extends PieceType> extends ToIntFunction<Move<T>> {
}
