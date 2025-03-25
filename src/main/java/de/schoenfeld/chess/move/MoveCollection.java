package de.schoenfeld.chess.move;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;

import java.util.*;

public class MoveCollection<T extends PieceType> implements List<Move<T>> {
    private final List<Move<T>> moves;
    private final Map<Square, List<Move<T>>> moveMap;
    private final Map<ChessPiece<T>, List<Move<T>>> pieceMap;

    public MoveCollection() {
        moves = new ArrayList<>();
        moveMap = new HashMap<>();
        pieceMap = new HashMap<>();
    }

    @SafeVarargs
    public static <T extends PieceType> MoveCollection<T> of(Move<T>... moves) {
        MoveCollection<T> mc = new MoveCollection<>();
        mc.addAll(Arrays.asList(moves));
        return mc;
    }

    public static <T extends PieceType> MoveCollection<T> of(Collection<Move<T>> moves) {
        MoveCollection<T> mc = new MoveCollection<>();
        mc.addAll(moves);
        return mc;
    }

    public List<Move<T>> getMovesTo(Square square) {
        return Collections.unmodifiableList(
                moveMap.getOrDefault(square, Collections.emptyList())
        );
    }

    public MoveCollection<T> getMovesForPiece(ChessPiece<T> piece) {
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
    public Iterator<Move<T>> iterator() {
        return new MoveIterator(moves.iterator());
    }

    @Override
    public Object[] toArray() {
        return moves.toArray();
    }

    @Override
    public <A> A[] toArray(A[] a) {
        return moves.toArray(a);
    }

    @Override
    public boolean add(Move<T> move) {
        moves.add(move);
        addToMaps(move);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        if (moves.remove(o)) {
            removeFromMaps((Move<T>) o);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(moves).containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Move<T>> c) {
        boolean modified = false;
        for (Move<T> move : c) {
            add(move);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Move<T>> c) {
        boolean modified = false;
        for (Move<T> move : c) {
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
    public Move<T> get(int index) {
        return moves.get(index);
    }

    @Override
    public Move<T> set(int index, Move<T> element) {
        Move<T> removed = moves.set(index, element);
        removeFromMaps(removed);
        addToMaps(element);
        return removed;
    }

    @Override
    public void add(int index, Move<T> element) {
        moves.add(index, element);
        addToMaps(element);
    }

    @Override
    public Move<T> remove(int index) {
        Move<T> removed = moves.remove(index);
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
    public ListIterator<Move<T>> listIterator() {
        return new MoveListIterator(moves.listIterator());
    }

    @Override
    public ListIterator<Move<T>> listIterator(int index) {
        return new MoveListIterator(moves.listIterator(index));
    }

    @Override
    public List<Move<T>> subList(int fromIndex, int toIndex) {
        return moves.subList(fromIndex, toIndex);
    }

    public boolean containsMoveTo(Square pieceSquare) {
        return moveMap.containsKey(pieceSquare);
    }

    private void addToMaps(Move<T> move) {
        moveMap.computeIfAbsent(move.to(), k -> new ArrayList<>()).add(move);
        pieceMap.computeIfAbsent(move.movedPiece(), k -> new ArrayList<>()).add(move);
    }

    private void removeFromMaps(Move<T> move) {
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
        Iterator<Move<T>> it = iterator();

        while (it.hasNext()) {
            Move<T> move = it.next();
            if (operationSet.contains(move) == remove) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    private class MoveIterator implements Iterator<Move<T>> {
        private final Iterator<Move<T>> iterator;
        private Move<T> current;

        MoveIterator(Iterator<Move<T>> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Move<T> next() {
            current = iterator.next();
            return current;
        }

        @Override
        public void remove() {
            iterator.remove();
            removeFromMaps(current);
        }
    }

    private class MoveListIterator extends MoveIterator implements ListIterator<Move<T>> {
        private final ListIterator<Move<T>> listIterator;
        private Move<T> lastReturned = null;

        MoveListIterator(ListIterator<Move<T>> listIterator) {
            super(listIterator);
            this.listIterator = listIterator;
        }

        @Override
        public Move<T> next() {
            lastReturned = super.next();
            return lastReturned;
        }

        @Override
        public Move<T> previous() {
            lastReturned = listIterator.previous();
            return lastReturned;
        }

        @Override
        public void set(Move<T> e) {
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
        public void add(Move<T> e) {
            listIterator.add(e);
            addToMaps(e);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        MoveCollection<?> that = (MoveCollection<?>) object;
        return Objects.equals(moves, that.moves) && Objects.equals(moveMap, that.moveMap) && Objects.equals(pieceMap, that.pieceMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moves, moveMap, pieceMap);
    }

    @Override
    public String toString() {
        return "MoveCollection{" +
                "moves=" + moves +
                ", moveMap=" + moveMap +
                ", pieceMap=" + pieceMap +
                '}';
    }
}