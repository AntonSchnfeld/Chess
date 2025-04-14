package de.schoenfeld.chesskit.board.tile;

import de.schoenfeld.chesskit.board.ChessBoard;

import java.io.Serializable;

/**
 * Represents a position on a {@link ChessBoard}.
 * <p>
 * This interface should be implemented by all classes that represent a specific type of
 * <code>Tile</code>, like {@link Square} or {@link Square8x8}. This class includes the ability
 * to check for adjacency of this <code>Tile</code> with another as well as the conversion to an algebraic
 * representation. Implementations are encouraged to ensure instances are interned or cached,
 * allowing fast identity and equality checks.
 * <p>
 * Example usage:
 * <pre>{@code
 *   Tile tile = Square8x8.of(1, 2);
 *   Tile anotherTile = Square.of(3, 4);
 *   tile.isAdjacent(anotherTile); // => false
 *   tile.toAlgebraicString(); // "b3"
 * }</pre>
 *
 * @author Anton Schoenfeld
 * @see <a href="https://en.wikipedia.org/wiki/Algebraic_notation_(chess)">Algebraic notation</a>
 */
public interface Tile extends Serializable {
    /**
     * Checks whether this tile is adjacent to another <code>Tile</code>.
     * <p>
     * A <code>Tile</code> is adjacent to another <code>Tile</code> when both are of the
     * same class and are at most one unit of its represented coordinate system apart from
     * one another.
     *
     * @param tile the <code>Tile</code> to check for adjacency
     * @return <code>true</code> if the <code>Tile</code>s are adjacent, <code>false</code>
     * otherwise
     */
    boolean isAdjacent(Tile tile);

    /**
     * Returns a {@link String} representation of this <code>Tile</code> in algebraic notation.
     * <p>
     * The standard form of algebraic notation may not be applicable to the coordinate system of
     * a <code>Tile</code> implementation. In that case, the implementation should define its own,
     * equivalent standard which can be used for PGN parsing and generation.
     *
     * @return a <code>String</code> representation of this tile in its algebraic form, for example, "b3"
     * @see <a href="https://en.wikipedia.org/wiki/Algebraic_notation_(chess)">Algebraic notation</a>
     * @see <a href="https://en.wikipedia.org/wiki/Portable_Game_Notation">PGN</a>
     */
    String toAlgebraicString();
}
