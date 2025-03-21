package de.schoenfeld.chess.model;

import de.schoenfeld.chess.move.Move;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record MoveHistory<T extends PieceType>(
        List<Move<T>> moves // Immutable list
) implements Serializable {

    public MoveHistory {
        moves = List.copyOf(moves); // Defensive copy
    }

    public MoveHistory() {
        this(List.of());
    }

    public MoveHistory<T> withMoveRecorded(Move<T> move) {
        List<Move<T>> newMoves = new ArrayList<>(moves);
        newMoves.add(move);
        return new MoveHistory<>(newMoves);
    }

    public MoveHistory<T> withoutLastMove() {
        if (moves.isEmpty()) return this;
        return new MoveHistory<>(moves.subList(0, moves.size() - 1));
    }

    public Move<T> getLastMove() {
        return moves.isEmpty() ? null : moves.getLast();
    }

    public int getMoveCount() {
        return moves.size();
    }
}