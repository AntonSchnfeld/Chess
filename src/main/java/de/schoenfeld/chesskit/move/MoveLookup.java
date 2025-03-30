package de.schoenfeld.chesskit.move;

import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.Square;

import java.util.*;

public class MoveLookup<T extends PieceType> implements List<Move<T>> {
    private final List<Move<T>> moves;
    private final Map<Square, List<Move<T>>> moveMap;

    public MoveLookup() {
        moves = new ArrayList<>();
        moveMap = new HashMap<>();
    }

    public MoveLookup(List<Move<T>> moves) {
        this.moves = new ArrayList<>(moves);
        moveMap = new HashMap<>();
        for (Move<T> move : moves)
            addToMaps(move);
    }

    public void removeAllMovesFrom(Square from) {
        if (!moveMap.containsKey(from)) return;
        List<Move<T>> movesToRemove = moveMap.get(from);
        moves.removeAll(movesToRemove);
        moveMap.remove(from);
    }

    public void replaceAllMovesFrom(Square from, List<Move<T>> replacement) {
        if (!moveMap.containsKey(from)) return;
        List<Move<T>> movesToRemove = moveMap.get(from);
        moves.removeAll(movesToRemove);
        moveMap.remove(from);
        moves.addAll(replacement);
        for (Move<T> move : replacement)
            addToMaps(move);
    }

    @SafeVarargs
    public static <T extends PieceType> MoveLookup<T> of(Move<T>... moves) {
        return new ImmutableMoveLookup<>(Arrays.asList(moves));
    }

    @SuppressWarnings("unchecked")
    public static <T extends PieceType> MoveLookup<T> of() {
        return (MoveLookup<T>) ImmutableMoveLookup.EMPTY;
    }

    public static <T extends PieceType> MoveLookup<T> of(Collection<Move<T>> moves) {
        return new ImmutableMoveLookup<>(new ArrayList<>(moves));
    }

    public List<Move<T>> getMovesTo(Square square) {
        return Collections.unmodifiableList(
                moveMap.getOrDefault(square, Collections.emptyList())
        );
    }

    public MoveLookup<T> getMovesFromSquare(Square from) {
        MoveLookup<T> movesFromSquare = new MoveLookup<>();
        for (Move<T> move : moves)
            if (move.from().equals(from)) movesFromSquare.add(move);
        return movesFromSquare;
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
        return moves.containsAll(c);
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
        if (c.isEmpty()) return false;
        moves.addAll(index, c);  // ✅ Single bulk insert
        c.forEach(this::addToMaps);  // ✅ Efficient batch map update
        return true;
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
    }

    private void removeFromMaps(Move<T> move) {
        if (move == null) return;

        Optional.ofNullable(moveMap.get(move.to()))
                .ifPresent(list -> {
                    list.remove(move);
                    if (list.isEmpty()) moveMap.remove(move.to());
                });
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
            lastReturned = e;
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
        MoveLookup<?> that = (MoveLookup<?>) object;
        return Objects.equals(moves, that.moves) && Objects.equals(moveMap, that.moveMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moves, moveMap);
    }

    @Override
    public String toString() {
        return "MoveCollection{" +
                "moves=" + moves +
                ", moveMap=" + moveMap +
                '}';
    }
}