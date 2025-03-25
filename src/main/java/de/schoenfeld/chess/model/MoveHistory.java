package de.schoenfeld.chess.model;

import de.schoenfeld.chess.move.Move;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MoveHistory<T extends PieceType> implements Serializable {
    @Serial
    private static final long serialVersionUID = -5209348645420429762L;

    private final List<Move<T>> moves;

    public MoveHistory(List<Move<T>> moves) {
        this.moves = new LinkedList<>(moves);
    }

    public MoveHistory() {
        this(List.of());
    }

    public List<Move<T>> getMoves() {
        return Collections.unmodifiableList(moves);
    }

    public void recordMove(Move<T> move) {
        moves.add(move);
    }

    public void removeLastMove() {
        moves.removeLast();
    }

    public Move<T> getLastMove() {
        return moves.isEmpty() ? null : moves.getLast();
    }

    public int getMoveCount() {
        return moves.size();
    }
}