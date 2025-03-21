package de.schoenfeld.chess.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the bounds of a chess board.
 * The bounds are immutable. The bounds are zero-based, i.e. the top-left corner is (0, 0).
 *
 * @param rows    The number of rows
 *                (must be at least 1)
 * @param columns The number of columns
 *                (must be at least 1)
 * @author Anton Schoenfeld
 */
public record ChessBoardBounds(int rows, int columns) implements Serializable {

    /**
     * Returns the bounds of a chess board with the given number of rows and columns.
     *
     * @param rows    The number of rows
     *                (must be at least 1)
     * @param columns The number of columns
     *                (must be at least 1)
     * @throws java.lang.IllegalArgumentException if rows is less than 1
     * @throws java.lang.IllegalArgumentException if columns is less than 1
     */
    public ChessBoardBounds {
        if (rows <= 0)
            throw new IllegalArgumentException("rows must be at least 1");
        if (columns <= 0)
            throw new IllegalArgumentException("columns must be at least 1");
    }

    /**
     * Returns all positions on the chess board.
     * The positions are returned in row-major order.
     * @return The list of all positions
     */
    public List<Square> allPositions() {
        ArrayList<Square> list = new ArrayList<>();
        list.ensureCapacity(rows * columns);

        for (int x = 0; x < rows; x++)
            for (int y = 0; y < columns; y++)
                list.add(new Square(x, y));

        return list;
    }

    /**
     * Returns whether the given position is contained in the bounds.
     * @param square The position to check
     * @return Whether the position is contained in the bounds
     */
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
