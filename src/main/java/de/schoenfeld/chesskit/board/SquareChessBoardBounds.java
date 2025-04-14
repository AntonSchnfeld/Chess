package de.schoenfeld.chesskit.board;

import de.schoenfeld.chesskit.board.tile.Square;

import java.util.ArrayList;
import java.util.List;

public record SquareChessBoardBounds(int rows, int columns) implements ChessBoardBounds<Square> {
    public SquareChessBoardBounds {
        if (rows <= 0)
            throw new IllegalArgumentException("rows must be at least 1");
        if (columns <= 0)
            throw new IllegalArgumentException("columns must be at least 1");
    }

    @Override
    public List<Square> getTiles() {
        ArrayList<Square> list = new ArrayList<>(getTileCount());

        for (int x = 0; x < rows; x++)
            for (int y = 0; y < columns; y++)
                list.add(Square.of(x, y));

        return list;
    }

    @Override
    public int getTileCount() {
        return rows * columns;
    }

    @Override
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
