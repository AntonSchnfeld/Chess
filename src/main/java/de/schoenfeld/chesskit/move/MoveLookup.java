package de.schoenfeld.chesskit.move;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.pool.Pool;

import java.util.*;

public class MoveLookup<T extends Tile, P extends PieceType> implements List<Move<T, P>>, RandomAccess {
    private final List<Move<T, P>> moves;
    private final Map<T, List<Move<T, P>>> moveMap;
    private final Pool<Move<T, P>> pool;

    public MoveLookup(Pool<Move<T, P>> pool, Collection<Move<T, P>> moves) {
        this.pool = pool;
        this.moves = new ArrayList<>(moves.size());
        for (Move<T, P> move : moves) {
            this.moves.add(Move.of(move));
        }
        moveMap = new HashMap<>();
        for (Move<T, P> move : this.moves)
            addToMaps(move);
    }

    @SuppressWarnings("all")
    public MoveLookup(Collection<Move<T, P>> moves) {
        this((Pool<Move<T, P>>) ((Pool) Move.POOL), moves);
    }

    public MoveLookup() {
        this(List.of());
    }

    @SafeVarargs
    public static <T extends Tile, P extends PieceType> MoveLookup<T, P> of(Move<T, P>... moves) {
        return new ImmutableMoveLookup<>(Arrays.asList(moves));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Tile, P extends PieceType> MoveLookup<T, P> of() {
        return (MoveLookup<T, P>) ImmutableMoveLookup.EMPTY;
    }

    public static <T extends Tile, P extends PieceType> MoveLookup<T, P> of(Collection<Move<T, P>> moves) {
        return new ImmutableMoveLookup<>(new ArrayList<>(moves));
    }

    @Override
    public Move<T, P> get(int index) {
        return moves.get(index);
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
    public int indexOf(Object o) {
        return moves.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return moves.lastIndexOf(o);
    }

    public List<Move<T, P>> getMovesTo(T tile) {
        return Collections.unmodifiableList(
                moveMap.getOrDefault(tile, Collections.emptyList())
        );
    }

    public MoveLookup<T, P> getMovesFromSquare(T from) {
        MoveLookup<T, P> movesFromSquare = new MoveLookup<>();
        for (Move<T, P> move : moves)
            if (move.from().equals(from)) movesFromSquare.add(move);
        return movesFromSquare;
    }

    @Override
    public List<Move<T, P>> subList(int fromIndex, int toIndex) {
        return moves.subList(fromIndex, toIndex);
    }

    @Override
    public boolean contains(Object o) {
        return moves.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(moves).containsAll(c);
    }

    public boolean containsMoveTo(T tile) {
        return moveMap.containsKey(tile);
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
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        if (moves.remove(o)) {
            removeFromMaps((Move<T, P>) o);
            return true;
        }
        return false;
    }

    public void removeAllMovesFrom(T from) {
        if (!moveMap.containsKey(from)) return;
        List<Move<T, P>> movesToRemove = moveMap.get(from);
        moves.removeAll(movesToRemove);
        moveMap.remove(from);
        for (Move<T, P> move : movesToRemove) pool.release(move);
    }

    public void replaceAllMovesFrom(T from, List<Move<T, P>> replacement) {
        if (!moveMap.containsKey(from)) return;
        removeAllMovesFrom(from);
        for (Move<T, P> move : replacement)
            addToMaps(move);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return bulkModify(c, true);
    }

    @Override
    public void clear() {
        for (int i = 0; i < moves.size(); i++) pool.release(moves.get(i));
        moves.clear();
        moveMap.clear();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return bulkModify(c, false);
    }

    @Override
    public Move<T, P> remove(int index) {
        Move<T, P> removed = moves.remove(index);
        removeFromMaps(removed);
        return removed;
    }

    private void removeFromMaps(Move<T, P> move) {
        if (move == null) return;

        Optional.ofNullable(moveMap.get(move.to()))
                .ifPresent(list -> {
                    list.remove(move);
                    pool.release(move);
                    if (list.isEmpty()) moveMap.remove(move.to());
                });
    }

    @Override
    public void add(int index, Move<T, P> element) {
        moves.add(index, element);
        addToMaps(element);
    }

    @Override
    public boolean add(Move<T, P> move) {
        moves.add(move);
        addToMaps(move);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Move<T, P>> c) {
        boolean modified = false;
        for (Move<T, P> move : c) {
            add(move);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Move<T, P>> c) {
        if (c.isEmpty()) return false;
        moves.addAll(index, c);  // ✅ Single bulk insert
        c.forEach(this::addToMaps);  // ✅ Efficient batch map update
        return true;
    }

    @Override
    public Move<T, P> set(int index, Move<T, P> element) {
        Move<T, P> removed = moves.set(index, element);
        removeFromMaps(removed);
        addToMaps(element);
        return removed;
    }

    @Override
    public void sort(Comparator<? super Move<T, P>> c) {
        moves.sort(c);
        moveMap.clear();
        for (Move<T, P> move : moves)
            addToMaps(move);
    }

    private void addToMaps(Move<T, P> move) {
        moveMap.computeIfAbsent(move.to(), k -> new ArrayList<>()).add(move);
    }

    private boolean bulkModify(Collection<?> c, boolean remove) {
        Set<?> operationSet = new HashSet<>(c);
        boolean modified = false;
        Iterator<Move<T, P>> it = iterator();

        while (it.hasNext()) {
            Move<T, P> move = it.next();
            if (operationSet.contains(move) == remove) {
                it.remove();
                pool.release(move);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Iterator<Move<T, P>> iterator() {
        return new MoveIterator(moves.iterator());
    }

    @Override
    public ListIterator<Move<T, P>> listIterator() {
        return new MoveListIterator(moves.listIterator());
    }

    @Override
    public ListIterator<Move<T, P>> listIterator(int index) {
        return new MoveListIterator(moves.listIterator(index));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        MoveLookup<?, ?> that = (MoveLookup<?, ?>) o;
        return Objects.equals(moves, that.moves) && Objects.equals(moveMap, that.moveMap) && Objects.equals(pool, that.pool);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(moves);
        result = 31 * result + Objects.hashCode(moveMap);
        result = 31 * result + Objects.hashCode(pool);
        return result;
    }

    @Override
    public String toString() {
        return "MoveLookup{" +
                "moves=" + moves +
                ", moveMap=" + moveMap +
                ", pool=" + pool +
                '}';
    }

    private class MoveIterator implements Iterator<Move<T, P>> {
        private final Iterator<Move<T, P>> iterator;
        private Move<T, P> current;

        MoveIterator(Iterator<Move<T, P>> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Move<T, P> next() {
            current = iterator.next();
            return current;
        }

        @Override
        public void remove() {
            iterator.remove();
            removeFromMaps(current);
        }
    }

    private class MoveListIterator extends MoveIterator implements ListIterator<Move<T, P>> {
        private final ListIterator<Move<T, P>> listIterator;
        private Move<T, P> lastReturned = null;

        MoveListIterator(ListIterator<Move<T, P>> listIterator) {
            super(listIterator);
            this.listIterator = listIterator;
        }

        @Override
        public Move<T, P> next() {
            lastReturned = super.next();
            return lastReturned;
        }

        @Override
        public Move<T, P> previous() {
            lastReturned = listIterator.previous();
            return lastReturned;
        }

        @Override
        public void set(Move<T, P> e) {
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
        public void add(Move<T, P> e) {
            listIterator.add(e);
            addToMaps(e);
        }
    }
}