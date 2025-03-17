package de.schoenfeld.chess.model;

import de.schoenfeld.chess.move.Move;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record MoveHistory(
        List<Move> moves // Immutable list
) implements Serializable {

    public MoveHistory {
        moves = List.copyOf(moves); // Defensive copy
    }

    public MoveHistory() {
        this(List.of());
    }

    public MoveHistory withMoveRecorded(Move move) {
        List<Move> newMoves = new ArrayList<>(moves);
        newMoves.add(move);
        return new MoveHistory(newMoves);
    }

    public MoveHistory withoutLastMove() {
        if (moves.isEmpty()) return this;
        return new MoveHistory(moves.subList(0, moves.size() - 1));
    }

    public Move getLastMove() {
        return moves.isEmpty() ? null : moves.getLast();
    }

    public List<Move> getAllMoves() {
        return Collections.unmodifiableList(moves);
    }

    public int getMoveCount() {
        return moves.size();
    }
}