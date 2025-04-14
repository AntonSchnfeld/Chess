package de.schoenfeld.chesskit.move;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.pool.Pool;
import de.schoenfeld.chesskit.model.pool.Poolable;
import de.schoenfeld.chesskit.move.components.MoveComponent;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Move<T extends Tile, P extends PieceType> implements Serializable, Poolable {
    public static final Pool<Move<?, ?>> POOL = new Pool<>() {
        @Override
        public Move<?, ?> create() {
            return new Move<>();
        }
    };
    @Serial
    private static final long serialVersionUID = 1921632465085309764L;
    // Store components in an array for space efficiency.
    private final ArrayList<MoveComponent<T, P>> components;
    private ChessPiece<P> movedPiece;
    private T from, to;

    private Move() {
        components = new ArrayList<>();
    }

    public Move(Move<T, P> move) {
        this.components = new ArrayList<>(move.components);
        this.movedPiece = move.movedPiece;
        this.from = move.from;
        this.to = move.to;
    }

    private Move(ChessPiece<P> movedPiece, T from, T to, MoveComponent<T, P>[] components) {
        this.components = new ArrayList<>(components.length);
        this.components.addAll(Arrays.asList(components));
        this.movedPiece = movedPiece;
        this.from = from;
        this.to = to;
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T extends Tile, P extends PieceType> Move<T, P> of(ChessPiece<P> movedPiece,
                                                                      T from,
                                                                      T to,
                                                                      MoveComponent<T, P>... components) {
        Move<T, P> move = (Move<T, P>) POOL.claim();
        move.movedPiece = movedPiece;
        move.from = from;
        move.to = to;
        move.components.addAll(Arrays.asList(components));
        return move;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Tile, P extends PieceType> Move<T, P> of(Move<T, P> other) {
        Move<T, P> move = (Move<T, P>) POOL.claim();
        move.movedPiece = other.movedPiece;
        move.from = other.from;
        move.to = other.to;
        move.components.addAll(other.components);
        return move;
    }

    public ChessPiece<P> movedPiece() {
        return movedPiece;
    }

    public T from() {
        return from;
    }

    public T to() {
        return to;
    }

    public <C extends MoveComponent<T, P>> C getComponent(Class<C> clazz) {
        for (int i = 0; i < components.size(); i++) {
            MoveComponent<T, P> comp = components.get(i);
            if (clazz.isInstance(comp)) {
                // The cast is safe because of the preceding check.
                return clazz.cast(comp);
            }
        }
        return null;
    }

    public void addComponent(MoveComponent<T, P> component) {
        components.add(component);
    }

    public void removeComponent(MoveComponent<T, P> component) {
        components.remove(component);
    }

    @Override
    public void reset() {
        from = null;
        to = null;
        components.clear();
    }

    @SuppressWarnings("all")
    public boolean hasComponent(Class<? extends MoveComponent> clazz) {
        for (int i = 0; i < components.size(); i++)
            if (clazz.isInstance(components.get(i)))
                return true;
        return false;
    }

    public List<MoveComponent<T, P>> getComponents() {
        return components;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Move<?, ?> move = (Move<?, ?>) object;
        return Objects.equals(movedPiece, move.movedPiece) &&
                Objects.equals(from, move.from) &&
                Objects.equals(to, move.to) &&
                Objects.equals(components, move.components);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(movedPiece, from, to);
        result = 31 * result + 31 * components.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Move{" +
                "components=" + components +
                ", movedPiece=" + movedPiece +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
