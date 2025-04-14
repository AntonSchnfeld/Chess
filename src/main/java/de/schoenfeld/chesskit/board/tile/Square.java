package de.schoenfeld.chesskit.board.tile;

import de.schoenfeld.chesskit.board.ChessBoard;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a generic two-dimensional <code>Tile</code> on a {@link ChessBoard}.
 * <p>
 * Unlike fixed-board implementations such as {@link Square8x8}, this class is flexible and can represent any
 * coordinate on an unbounded or dynamically sized board. Squares are identified by their zero-based
 * (x, y) coordinates, where <code>x = 0</code> maps to file 'a' and <code>y = 0</code> to rank 1 in algebraic notation.
 * <p>
 * Squares are created and cached via {@link #of(int, int)} to ensure identity uniqueness and reduce memory overhead
 * during board operations such as move generation.
 * <p>
 * Example usage:
 * <pre>{@code
 *   ChessBoard<Square, MyPieceType> board = new ChessBoard<>();
 *   Square square = Square.of(4, 1); // e2
 *   board.placePiece(square, MyPieceType.KNIGHT, Colour.WHITE);
 * }</pre>
 *
 * @author Anton Schoenfeld
 * @see Tile
 * @see Square8x8
 * @see de.schoenfeld.chesskit.board.ChessBoard
 * @see <a href="https://en.wikipedia.org/wiki/Algebraic_notation_(chess)">Algebraic notation</a>
 */
public class Square implements Tile {
    @Serial
    private static final long serialVersionUID = 5180203319197729993L;
    private static final Map<Long, Square> SQUARES = new HashMap<>();

    /**
     * Returns a cached {@link Square} instance for the given coordinates.
     * <p>
     * Coordinates must be non-negative. Calling this method repeatedly with the same parameters
     * returns the same object instance.
     *
     * @param x the file index
     * @param y the rank index
     * @return a unique {@link Square} for the given x and y coordinate
     * @throws IllegalArgumentException if x or y is negative
     */
    public static Square of(int x, int y) {
        long key = ((long) x << 32) | (y & 0xFFFFFFFFL);
        return SQUARES.computeIfAbsent(key, k -> new Square(x, y));
    }

    protected final int x;
    protected final int y;

    protected Square(int x, int y) {
        if (x < 0)
            throw new IllegalArgumentException("x < 0");
        if (y < 0)
            throw new IllegalArgumentException("y < 0");
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate (file index) of this square.
     *
     * @return the x position
     */
    public int x() {
        return x;
    }

    /**
     * Returns the y-coordinate (rank index) of this square.
     *
     * @return the y position
     */
    public int y() {
        return y;
    }

    /**
     * Checks whether this square is adjacent to another {@link Tile}.
     * <p>
     * Two squares are considered adjacent if both are {@code Square} instances and
     * the difference in both x and y coordinates is at most 1.
     *
     * @param tile the tile to compare to
     * @return {@code true} if the tiles are adjacent; {@code false} otherwise
     */
    @Override
    public boolean isAdjacent(Tile tile) {
        if (!(tile instanceof Square other)) return false;
        int xDifference = Math.abs(other.x - x);
        int yDifference = Math.abs(other.y - y);
        return xDifference <= 1 && yDifference <= 1;
    }

    /**
     * Converts this square into its algebraic string representation.
     * <p>
     * The algebraic format is computed as {@code (char)('a' + x)} followed by {@code (y + 1)},
     * for example, {@code Square.of(4, 1)} → {@code "e2"}.
     *
     * @return the square's algebraic notation
     */
    @Override
    public String toAlgebraicString() {
        return String.format("%c%d", 'a' + x, y + 1);
    }

    public Square offset(Square square) {
        if (square == null)
            throw new IllegalArgumentException("square must not be null");
        return Square.of(square.x + x, square.y + y);
    }

    public Square offset(int dx, int dy) {
        return Square.of(dx + x, dy + y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Square square = (Square) o;
        return x == square.x && y == square.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    /**
     * Returns a string representation of the square in debug-friendly format.
     *
     * @return a string like {@code Square{x=3, y=2}}
     */
    @Override
    public String toString() {
        return "Square{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
