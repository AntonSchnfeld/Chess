package de.schoenfeld.chess;

import java.util.List;

public interface MoveHistory {
    void recordMove(Move move);

    /**
     * Returns the last {@link Move} and then deletes it from the cache.
     *
     * @return the last {@link Move}
     */
    Move undoLastMove();

    /**
     * @return the last recorded {@link Move}
     */
    Move getLastMove();

    /**
     * @return a {@link List<Move>} with all the recorded {@link Move}s in chronological order.
     */
    List<Move> getAllMoves();

    /**
     * @return the number of {@link Move}s that have been recorded.
     */
    int getMoveCount();
}
