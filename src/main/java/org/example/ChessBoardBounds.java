package org.example;

public record ChessBoardBounds(int rows, int columns) {

    public ChessBoardBounds {
        if (rows <= 0)
            throw new IllegalArgumentException("rows must be at least 1");
        if (columns <= 0)
            throw new IllegalArgumentException("columns must be at least 1");
    }

    public boolean contains(Position position) {
        if (position == null) {
            return false;
        }
        return position.x() >= 0 &&
                rows > position.x() &&
                position.y() >= 0 &&
                columns > position.y();
    }
}
