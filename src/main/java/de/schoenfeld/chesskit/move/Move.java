package de.schoenfeld.chesskit.move;

import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.Square;
import de.schoenfeld.chesskit.model.pool.Pool;
import de.schoenfeld.chesskit.model.pool.Poolable;
import de.schoenfeld.chesskit.move.components.MoveComponent;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Move<T extends PieceType> implements Serializable, Poolable {
    @Serial
    private static final long serialVersionUID = 1921632465085309764L;
    public static final Pool<Move<?>> POOL = new Pool<>() {
        @Override
        public Move<?> create() {
            return new Move<>();
        }
    };

    // Store components in an array for space efficiency.
    private final ArrayList<MoveComponent<T>> components;
    private ChessPiece<T> movedPiece;
    private Square from, to;

    private Move() {
        components = new ArrayList<>();
    }

    private Move(ChessPiece<T> movedPiece, Square from, Square to, MoveComponent<T>[] components) {
        this.components = new ArrayList<>(components.length);
        this.components.addAll(Arrays.asList(components));
        this.movedPiece = movedPiece;
        this.from = from;
        this.to = to;
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T extends PieceType> Move<T> claim(ChessPiece<T> movedPiece, Square from, Square to,
                                                      MoveComponent<T>... components) {
        Move<T> move = (Move<T>) POOL.claim();
        move.movedPiece = movedPiece;
        move.from = from;
        move.to = to;
        for (int i = 0; i < components.length; i++) move.components.add(components[i]);
        return move;
    }

    public static void release(Move<?> move) {
        move.reset();
        POOL.release(move);
    }

    public ChessPiece<T> movedPiece() {
        return movedPiece;
    }

    public Square from() {
        return from;
    }

    public Square to() {
        return to;
    }

    public <C extends MoveComponent<T>> C getComponent(Class<C> clazz) {
        for (int i = 0; i < components.size(); i++) {
            MoveComponent<T> comp = components.get(i);
            if (clazz.isInstance(comp)) {
                // The cast is safe because of the check above.
                return clazz.cast(comp);
            }
        }
        return null;
    }

    public void addComponent(MoveComponent<T> component) {
        components.add(component);
    }

    public void removeComponent(MoveComponent<T> component) {
        components.remove(component);
    }

    @Override
    public void reset() {
        from = null;
        to = null;
        components.clear();
    }

    public boolean hasComponent(Class<? extends MoveComponent> clazz) {
        for (int i = 0; i < components.size(); i++)
            if (clazz.isInstance(components.get(i)))
                return true;
        return false;
    }

    public List<MoveComponent<T>> getComponents() {
        return components;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Move<?> move = (Move<?>) object;
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
