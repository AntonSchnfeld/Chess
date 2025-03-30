package de.schoenfeld.chesskit.move;

import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.Square;

import java.util.Collection;
import java.util.List;

final class ImmutableMoveLookup<T extends PieceType> extends MoveLookup<T> {
    static final ImmutableMoveLookup<?> EMPTY = new ImmutableMoveLookup<>();

    ImmutableMoveLookup() {
        super();
    }

    ImmutableMoveLookup(List<Move<T>> moves) {
        super(moves);
    }

    @Override
    public Move<T> remove(int i) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public void add(int index, Move<T> move) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public Move<T> set(int index, Move<T> move) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public boolean addAll(Collection<? extends Move<T>> c) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public boolean addAll(int index, Collection<? extends Move<T>> c) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public void removeAllMovesFrom(Square square) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public void replaceAllMovesFrom(Square square, List<Move<T>> replacement) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }
}
