package de.schoenfeld.chess.data;

import java.util.ArrayList;
import java.util.List;

public record ChessBoardBounds(int rows, int columns) {

    public ChessBoardBounds {
        if (rows <= 0)
            throw new IllegalArgumentException("rows must be at least 1");
        if (columns <= 0)
            throw new IllegalArgumentException("columns must be at least 1");
    }

    public List<Position> allPositions() {
        ArrayList<Position> list = new ArrayList<>();
        list.ensureCapacity(rows * columns);

        for (int x = 0; x < rows; x++)
            for (int y = 0; y < columns; y++)
                list.add(new Position(x, y));

        return list;
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
