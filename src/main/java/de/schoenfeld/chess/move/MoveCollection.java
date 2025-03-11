package de.schoenfeld.chess.move;

import de.schoenfeld.chess.model.Position;

import java.util.*;

public class MoveCollection implements Set<Move> {
    private final Set<Move> moves;
    private final Map<Position, Move> moveMap; // Fast lookup by target position

    public MoveCollection() {
        moves = new HashSet<>();
        moveMap = new HashMap<>();
    }

    public static MoveCollection of(Move... moves) {
        MoveCollection moveCollection = new MoveCollection();
        moveCollection.addAll(Arrays.asList(moves));
        return moveCollection;
    }

    @Override
    public int size() {
        return moves.size();
    }

    @Override
    public boolean isEmpty() {
        return moves.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return moves.contains(o);
    }

    public Move getMoveTo(Position to) {
        return moveMap.get(to);
    }

    public boolean containsMoveTo(Position to) {
        return moveMap.containsKey(to);
    }

    @Override
    public Iterator<Move> iterator() {
        return moves.iterator();
    }

    @Override
    public Object[] toArray() {
        return moves.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return moves.toArray(a);
    }

    @Override
    public boolean add(Move move) {
        if (moves.add(move)) {
            moveMap.put(move.to(), move);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof Move move) {
            moveMap.remove(move.to());
            return moves.remove(move);
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return moves.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Move> c) {
        boolean modified = false;
        for (Move move : c) {
            if (add(move)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = moves.retainAll(c);
        moveMap.clear();
        for (Move move : moves) {
            moveMap.put(move.to(), move);
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object obj : c) {
            if (obj instanceof Move move) {
                if (remove(move)) {
                    modified = true;
                }
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        moves.clear();
        moveMap.clear();
    }
}
