package de.schoenfeld.chesskit.move;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.PieceType;

import java.util.Collection;
import java.util.List;

final class ImmutableMoveLookup<T extends Tile, P extends PieceType> extends MoveLookup<T, P> {
    static final ImmutableMoveLookup<?, ?> EMPTY = new ImmutableMoveLookup<>();

    ImmutableMoveLookup() {
        super();
    }

    ImmutableMoveLookup(List<Move<T, P>> moves) {
        super(moves);
    }

    @Override
    public Move<T, P> remove(int i) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public void add(int index, Move<T, P> move) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public Move<T, P> set(int index, Move<T, P> move) {
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
    public boolean addAll(Collection<? extends Move<T, P>> c) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public boolean addAll(int index, Collection<? extends Move<T, P>> c) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public void removeAllMovesFrom(T tile) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }

    @Override
    public void replaceAllMovesFrom(T tile, List<Move<T, P>> replacement) {
        throw new UnsupportedOperationException("This MoveLookup is immutable!");
    }
}
