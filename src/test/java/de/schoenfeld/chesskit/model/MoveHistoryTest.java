package de.schoenfeld.chesskit.model;

import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.move.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class MoveHistoryTest {
    private MoveHistory<Square8x8, StandardPieceType> tested;

    @BeforeEach
    public void setup() {
        tested = new MoveHistory<>();
    }

    @Test
    public void givenEmptyHistory_whenCreated_thenHasNoMoves() {
        assertEquals(0, tested.getMoveCount());
        assertNull(tested.getLastMove());
        assertTrue(tested.getMoves().isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenMoveHistory_whenMoveRecorded_thenNewHistoryHasMove() {
        Move<Square8x8, StandardPieceType> move = mock(Move.class);
        tested.recordMove(move);

        assertEquals(1, tested.getMoveCount());
        assertEquals(move, tested.getLastMove());
    }

    @Test
    public void givenMoveHistory_when_thenNewMoveHist() {
        tested.recordMove(Move.of(null, null, null));
        assertEquals(1, tested.getMoveCount());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenMoveHistoryWithMoves_whenLastMoveRemoved_thenNewHistoryHasOneLessMove() {
        Move<Square8x8, StandardPieceType> move1 = mock(Move.class);
        Move<Square8x8, StandardPieceType> move2 = mock(Move.class);
        MoveHistory<Square8x8, StandardPieceType> history = new MoveHistory<>();
        history.recordMove(move1);
        history.recordMove(move2);
        history.removeLastMove();

        assertEquals(1, history.getMoveCount());
        assertEquals(move1, history.getLastMove());
    }
}
