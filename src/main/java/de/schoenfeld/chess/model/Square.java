package de.schoenfeld.chess.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record Square(int x, int y) {
    private static final Map<Long, Square> POOL = new ConcurrentHashMap<>(128);

    public static final Square
            a1 = Square.of(0, 7), a2 = Square.of(1, 7),
            a3 = Square.of(2, 7), a4 = Square.of(3, 7),
            a5 = Square.of(4, 7), a6 = Square.of(5, 7),
            a7 = Square.of(6, 7), a8 = Square.of(7, 7),
            b1 = Square.of(0, 6), b2 = Square.of(1, 6),
            b3 = Square.of(2, 6), b4 = Square.of(3, 6),
            b5 = Square.of(4, 6), b6 = Square.of(5, 6),
            b7 = Square.of(6, 6), b8 = Square.of(7, 6),
            c1 = Square.of(0, 5), c2 = Square.of(1, 5),
            c3 = Square.of(2, 5), c4 = Square.of(3, 5),
            c5 = Square.of(4, 5), c6 = Square.of(5, 5),
            c7 = Square.of(6, 5), c8 = Square.of(7, 5),
            d1 = Square.of(0, 4), d2 = Square.of(1, 4),
            d3 = Square.of(2, 4), d4 = Square.of(3, 4),
            d5 = Square.of(4, 4), d6 = Square.of(5, 4),
            d7 = Square.of(6, 4), d8 = Square.of(7, 4),
            e1 = Square.of(0, 3), e2 = Square.of(1, 3),
            e3 = Square.of(2, 3), e4 = Square.of(3, 3),
            e5 = Square.of(4, 3), e6 = Square.of(5, 3),
            e7 = Square.of(6, 3), e8 = Square.of(7, 3),
            f1 = Square.of(0, 2), f2 = Square.of(1, 2),
            f3 = Square.of(2, 2), f4 = Square.of(3, 2),
            f5 = Square.of(4, 2), f6 = Square.of(5, 2),
            f7 = Square.of(6, 2), f8 = Square.of(7, 2),
            g1 = Square.of(0, 1), g2 = Square.of(1, 1),
            g3 = Square.of(2, 1), g4 = Square.of(3, 1),
            g5 = Square.of(4, 1), g6 = Square.of(5, 1),
            g7 = Square.of(6, 1), g8 = Square.of(7, 1),
            h1 = Square.of(0, 0), h2 = Square.of(1, 0),
            h3 = Square.of(2, 0), h4 = Square.of(3, 0),
            h5 = Square.of(4, 0), h6 = Square.of(5, 0),
            h7 = Square.of(6, 0), h8 = Square.of(7, 0);


    public static Square of(int x, int y) {
        long key = ((long) x << 32) | (y & 0xFFFFFFFFL); // Unique key using bitwise operations
        return POOL.computeIfAbsent(key, k -> new Square(x, y));
    }

    public Square offset(int dx, int dy) {
        return new Square(x + dx, y + dy);
    }

    public Square offset(Square square) {
        if (square == null)
            throw new IllegalArgumentException("position must not be null");
        return new Square(square.x + x, square.y + y);
    }

    public String toAlgebraic() {
        return String.format("%c%d", 'a' + x, y + 1);
    }
}
