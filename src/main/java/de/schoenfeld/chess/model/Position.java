package de.schoenfeld.chess.model;

/**
 * Represents a position on a chess board.
 * The position is zero-based, i.e. the top-left corner is (0, 0).
 * The position is immutable. The position can be converted to an algebraic
 * notation using {@link #toAlgebraic()}. The algebraic notation is 1-based,
 * i.e. the top-left corner is a1.
 *
 * @param x The x-coordinate (0-based)
 * @param y The y-coordinate (0-based)
 * @author Anton Schoenfeld
 */
public record Position(int x, int y) {

    /**
     * Returns a position with the given coordinates.
     *
     * @param x The x-coordinate (0-based)
     * @param y The y-coordinate (0-based)
     * @return The new position
     */
    public static Position of(int x, int y) {
        return new Position(x, y);
    }

    /**
     * Returns a position that is offset by the given amount.
     * @param dx The x-offset
     *           (positive values move to the right, negative values move to the left)
     * @param dy The y-offset
     *           (positive values move down, negative values move up)
     */
    public Position offset(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }

    /**
     * Returns a position that is offset by the given position.
     * @param position The position to offset by
     * @throws java.lang.IllegalArgumentException if position is null
     */
    public Position offset(Position position) {
        if (position == null)
            throw new IllegalArgumentException("position must not be null");
        return new Position(position.x + x, position.y + y);
    }

    /**
     * Returns the algebraic notation of the position.
     * The algebraic notation is 1-based, i.e. the top-left corner is a1.
     * @return The algebraic notation of the position
     */
    public String toAlgebraic() {
        return String.format("%c%d", 'a' + x, y + 1);
    }
}
