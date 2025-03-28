package de.schoenfeld.chesskit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record ChessBoardBounds(int rows, int columns) implements Serializable {
    public ChessBoardBounds {
        if (rows <= 0)
            throw new IllegalArgumentException("rows must be at least 1");
        if (columns <= 0)
            throw new IllegalArgumentException("columns must be at least 1");
    }

    public List<Square> allPositions() {
        ArrayList<Square> list = new ArrayList<>();
        list.ensureCapacity(rows * columns);

        for (int x = 0; x < rows; x++)
            for (int y = 0; y < columns; y++)
                list.add(new Square(x, y));

        return list;
    }

    public boolean contains(Square square) {
        if (square == null) {
            return false;
        }
        return square.x() >= 0 &&
                rows > square.x() &&
                square.y() >= 0 &&
                columns > square.y();
    }
}
