package de.schoenfeld.chesskit.model;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.move.Move;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MoveHistory<T extends Tile, P extends PieceType> implements Serializable {
    @Serial
    private static final long serialVersionUID = -5209348645420429762L;

    private final List<Move<T, P>> moves;

    public MoveHistory(List<Move<T, P>> moves) {
        this.moves = new LinkedList<>(moves);
    }

    public MoveHistory() {
        this(List.of());
    }

    public List<Move<T, P>> getMoves() {
        return Collections.unmodifiableList(moves);
    }

    public void recordMove(Move<T, P> move) {
        moves.add(new Move<>(move));
    }

    public void removeLastMove() {
        moves.removeLast();
    }

    public Move<T, P> getLastMove() {
        return moves.isEmpty() ? null : moves.getLast();
    }

    public int getMoveCount() {
        return moves.size();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        MoveHistory<?, ?> that = (MoveHistory<?, ?>) object;
        return Objects.equals(moves, that.moves);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(moves);
    }

    @Override
    public String toString() {
        return "MoveHistory{" +
                "moves=" + moves +
                '}';
    }
}