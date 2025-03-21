package de.schoenfeld.chess.move;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.Square;

import java.util.*;

public class MoveCollection implements List<Move> {
    private final List<Move> moves;
    private final Map<Square, List<Move>> moveMap;
    private final Map<ChessPiece, List<Move>> pieceMap;

    public MoveCollection() {
        moves = new ArrayList<>();
        moveMap = new HashMap<>();
        pieceMap = new HashMap<>();
    }

    public static MoveCollection of(Move... moves) {
        MoveCollection mc = new MoveCollection();
        mc.addAll(Arrays.asList(moves));
        return mc;
    }

    public static MoveCollection of(Collection<Move> moves) {
        MoveCollection mc = new MoveCollection();
        mc.addAll(moves);
        return mc;
    }

    public List<Move> getMovesTo(Square square) {
        return Collections.unmodifiableList(
                moveMap.getOrDefault(square, Collections.emptyList())
        );
    }

    public MoveCollection getMovesForPiece(ChessPiece piece) {
        return MoveCollection.of(
                pieceMap.getOrDefault(piece, Collections.emptyList())
        );
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

    @Override
    public Iterator<Move> iterator() {
        return new MoveIterator(moves.iterator());
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
        moves.add(move);
        addToMaps(move);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (moves.remove(o)) {
            removeFromMaps((Move) o);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(moves).containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Move> c) {
        boolean modified = false;
        for (Move move : c) {
            add(move);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Move> c) {
        boolean modified = false;
        for (Move move : c) {
            add(index++, move);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return bulkModify(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return bulkModify(c, false);
    }

    @Override
    public void clear() {
        moves.clear();
        moveMap.clear();
        pieceMap.clear();
    }

    @Override
    public Move get(int index) {
        return moves.get(index);
    }

    @Override
    public Move set(int index, Move element) {
        Move removed = moves.set(index, element);
        removeFromMaps(removed);
        addToMaps(element);
        return removed;
    }

    @Override
    public void add(int index, Move element) {
        moves.add(index, element);
        addToMaps(element);
    }

    @Override
    public Move remove(int index) {
        Move removed = moves.remove(index);
        removeFromMaps(removed);
        return removed;
    }

    @Override
    public int indexOf(Object o) {
        return moves.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return moves.lastIndexOf(o);
    }

    @Override
    public ListIterator<Move> listIterator() {
        return new MoveListIterator(moves.listIterator());
    }

    @Override
    public ListIterator<Move> listIterator(int index) {
        return new MoveListIterator(moves.listIterator(index));
    }

    @Override
    public List<Move> subList(int fromIndex, int toIndex) {
        return moves.subList(fromIndex, toIndex);
    }

    public boolean containsMoveTo(Square pieceSquare) {
        return moveMap.containsKey(pieceSquare);
    }

    private void addToMaps(Move move) {
        moveMap.computeIfAbsent(move.to(), k -> new ArrayList<>()).add(move);
        pieceMap.computeIfAbsent(move.movedPiece(), k -> new ArrayList<>()).add(move);
    }

    private void removeFromMaps(Move move) {
        if (move == null) return;

        synchronized (this) {
            Optional.ofNullable(moveMap.get(move.to()))
                    .ifPresent(list -> {
                        list.remove(move);
                        if (list.isEmpty()) moveMap.remove(move.to());
                    });

            Optional.ofNullable(pieceMap.get(move.movedPiece()))
                    .ifPresent(list -> {
                        list.remove(move);
                        if (list.isEmpty()) pieceMap.remove(move.movedPiece());
                    });
        }
    }

    private boolean bulkModify(Collection<?> c, boolean remove) {
        Set<?> operationSet = new HashSet<>(c);
        boolean modified = false;
        Iterator<Move> it = iterator();

        while (it.hasNext()) {
            Move move = it.next();
            if (operationSet.contains(move) == remove) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    private class MoveIterator implements Iterator<Move> {
        private final Iterator<Move> iterator;
        private Move current;

        MoveIterator(Iterator<Move> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Move next() {
            current = iterator.next();
            return current;
        }

        @Override
        public void remove() {
            iterator.remove();
            removeFromMaps(current);
        }
    }

    private class MoveListIterator extends MoveIterator implements ListIterator<Move> {
        private final ListIterator<Move> listIterator;
        private Move lastReturned = null;

        MoveListIterator(ListIterator<Move> listIterator) {
            super(listIterator);
            this.listIterator = listIterator;
        }

        @Override
        public Move next() {
            lastReturned = super.next();
            return lastReturned;
        }

        @Override
        public Move previous() {
            lastReturned = listIterator.previous();
            return lastReturned;
        }

        @Override
        public void set(Move e) {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            listIterator.set(e);
            removeFromMaps(lastReturned);
            addToMaps(e);
            lastReturned = null;
        }

        @Override
        public boolean hasPrevious() {
            return listIterator.hasPrevious();
        }

        @Override
        public int nextIndex() {
            return listIterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return listIterator.previousIndex();
        }

        @Override
        public void add(Move e) {
            listIterator.add(e);
            addToMaps(e);
        }
    }
}