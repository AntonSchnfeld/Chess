package de.schoenfeld.chesskit.board.tile;

import de.schoenfeld.chesskit.board.ChessBoard;

import java.io.Serial;

/**
 * Represents an immutable tile on a standard 8x8 {@link ChessBoard}.
 * <p>
 * This class extends {@link Square} and specializes it by constraining all coordinates to
 * the 8×8 board used in standard chess. All instances are cached and created eagerly in a
 * static initializer, ensuring that there's exactly one {@code Square8x8} instance per (x, y)
 * coordinate within bounds [0, 7].
 * <p>
 * This allows for fast identity comparisons and zero allocations during move generation.
 * <p>
 * Example usage:
 * <pre>{@code
 *   Square8x8 e2 = Square8x8.of(4, 1); // returns the square for e2
 *   Square8x8 d3 = e2.offset(-1, 1);   // one square diagonally up-left
 * }</pre>
 *
 * @see Square
 * @see Tile
 * @see ChessBoard
 * @author Anton Schoenfeld
 */

public final class Square8x8 extends Square {
    // Row-major
    private static final Square8x8[] SQUARES = new Square8x8[64];
    @Serial
    private static final long serialVersionUID = 352107431094914162L;

    static {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                SQUARES[y + x * 8] = new Square8x8(x, y);
            }
        }
    }

    private Square8x8(int x, int y) {
        super(x, y);
        if (x >= 8 || y >= 8)
            throw new IllegalArgumentException("Invalid square coordinates: " + x + "," + y);
    }

    /**
     * Returns the unique {@link Square8x8} instance at the given coordinates.
     *
     * @param x the file index
     * @param y the rank index
     * @return the {@link Square8x8} instance at the specified {@code x} and {@code y} coordinates
     * @throws IllegalArgumentException if x or y are outside [0, 7]
     */
    public static Square8x8 of(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8)
            throw new IllegalArgumentException("Invalid square coordinates: " + x + "," + y);
        return SQUARES[y + x * 8];
    }

    /**
     * Returns a new square offset from this one by the given delta values.
     *
     * @param dx the horizontal offset
     * @param dy the vertical offset
     * @return the square at <code>x + dx</code> and <code>y + dy</code>
     * @throws IllegalArgumentException if the resulting coordinates are outside [0, 7]
     */
    public Square8x8 offset(int dx, int dy) {
        return Square8x8.of(x + dx, y + dy);
    }

    /**
     * Returns a new square offset by the coordinates of another square.
     *
     * @param square8x8 the offset square
     * @return the square at <code>this.x + dx</code>, <code>this.y + dy</code>
     * @throws IllegalArgumentException if the resulting coordinates are outside [0, 7]
     * @throws NullPointerException if the given square is {@code null}
     */
    public Square8x8 offset(Square8x8 square8x8) {
        if (square8x8 == null)
            throw new IllegalArgumentException("square8x8 must not be null");
        return Square8x8.of(square8x8.x + x, square8x8.y + y);
    }

    /**
     * Determines whether this square is adjacent to another {@link Square8x8}.
     * <p>
     * Two squares are adjacent if the difference in both x and y coordinates is at most 1.
     * This method only returns {@code true} if the other tile is also a {@link Square8x8}.
     *
     * @param tile the tile to check
     * @return {@code true} if adjacent, {@code false} otherwise
     */
    @Override
    public boolean isAdjacent(Tile tile) {
        if (!(tile instanceof Square8x8 other)) return false;
        int xDifference = Math.abs(other.x - x);
        int yDifference = Math.abs(other.y - y);
        return xDifference <= 1 && yDifference <= 1;
    }
}
