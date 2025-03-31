package de.schoenfeld.chesskit.model.pool;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Pool<T extends Poolable> {
    private static final int DEFAULT_CAPACITY = 1000;

    private final ArrayList<T> pool;
    private int size;

    public Pool(int initialCapacity) {
        pool = new ArrayList<>(initialCapacity);
        size = 0;
    }

    public Pool() {
        this(10);
    }

    public T claim() {
        if (size <= 0) {
            return create();
        }
        return pool.remove(--size);
    }

    public void release(T object) {
        pool.add(object);
        size++;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return pool.size();
    }

    public void clear() {
        pool.clear();
        size = 0;
    }

    public void trim() {
        pool.trimToSize();
        size = pool.size();
    }

    public abstract T create();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Pool<?> pool1 = (Pool<?>) o;
        return size == pool1.size && Objects.equals(pool, pool1.pool);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(pool);
        result = 31 * result + size;
        return result;
    }

    @Override
    public String toString() {
        return "Pool{" +
                "pool=" + pool +
                ", size=" + size +
                '}';
    }
}
