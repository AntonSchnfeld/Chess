package de.schoenfeld.chesskit.model;

/**
 * Represents the two player colors in a game of chess: {@code WHITE} and {@code BLACK}.
 * <p>
 * This enum is used to indicate which player a piece belongs to, whose turn it is,
 * or to determine color-based logic in game rules and UI rendering. It provides a
 * convenient method to retrieve the opposing color.
 * <p>
 * Example usage:
 * <pre>{@code
 *   Color player = Color.WHITE;
 *   Color opponent = player.opposite(); // -> Color.BLACK
 * }</pre>
 *
 * @author Anton Schoenfeld
 */
public enum Color {
    WHITE, BLACK;

    /**
     * Returns the opposing {@code Color}.
     * <p>
     * This method allows easy toggling between {@code WHITE} and {@code BLACK},
     * which is useful for turn-switching, evaluating threats, or determining the enemy side.
     * <p>
     * Example usage:
     * <pre>{@code
     *   Color current = Color.WHITE;
     *   Color next = current.opposite(); // -> Color.BLACK
     * }</pre>
     *
     * @return the opposite color (in other words, {@code BLACK} if this is {@code WHITE}, and vice versa)
     */
    public Color opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
}
