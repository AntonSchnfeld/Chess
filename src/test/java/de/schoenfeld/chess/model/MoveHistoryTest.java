package de.schoenfeld.chess.model;

import de.schoenfeld.chess.move.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class MoveHistoryTest {
    private MoveHistory tested;

    @BeforeEach
    public void setup() {
        tested = new MoveHistory();
    }

    @Test
    public void givenEmptyHistory_whenCreated_thenHasNoMoves() {
        assertEquals(0, tested.getMoveCount());
        assertNull(tested.getLastMove());
        assertTrue(tested.moves().isEmpty());
    }

    @Test
    public void givenMoveHistory_whenMoveRecorded_thenNewHistoryHasMove() {
        Move move = mock(Move.class);
        MoveHistory newHistory = tested.withMoveRecorded(move);

        assertNotSame(tested, newHistory);
        assertEquals(1, newHistory.getMoveCount());
        assertEquals(move, newHistory.getLastMove());
    }

    @Test
    public void givenMoveHistory_when_thenNewMoveHist() {
        tested = tested.withMoveRecorded(Move.of(null, null, null));
        assertEquals(1, tested.getMoveCount());
    }

    @Test
    public void givenMoveHistoryWithMoves_whenLastMoveRemoved_thenNewHistoryHasOneLessMove() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        MoveHistory history = new MoveHistory().withMoveRecorded(move1).withMoveRecorded(move2);
        MoveHistory reducedHistory = history.withoutLastMove();

        assertNotSame(history, reducedHistory);
        assertEquals(1, reducedHistory.getMoveCount());
        assertEquals(move1, reducedHistory.getLastMove());
    }
}
