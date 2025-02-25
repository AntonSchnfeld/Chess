package de.schoenfeld.chess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MoveCollectionTest {

    private MoveCollection moveCollection;
    private Move mockMove1;
    private Move mockMove2;
    private Position mockPosition1;
    private Position mockPosition2;

    @BeforeEach
    void setUp() {
        moveCollection = new MoveCollection();
        mockMove1 = mock(Move.class);
        mockMove2 = mock(Move.class);
        mockPosition1 = mock(Position.class);
        mockPosition2 = mock(Position.class);
        when(mockMove1.to()).thenReturn(mockPosition1);
        when(mockMove2.to()).thenReturn(mockPosition2);
    }

    @Test
    void givenMove_whenAdd_thenContainsMove() {
        assertTrue(moveCollection.add(mockMove1));
        assertTrue(moveCollection.contains(mockMove1));
        assertEquals(mockMove1, moveCollection.getMoveTo(mockPosition1));
    }

    @Test
    void givenDuplicateMove_whenAdd_thenFails() {
        assertTrue(moveCollection.add(mockMove1));
        assertFalse(moveCollection.add(mockMove1)); // Duplicate should not be added
        assertEquals(1, moveCollection.size());
    }

    @Test
    void givenMove_whenRemove_thenNotContained() {
        moveCollection.add(mockMove1);
        assertTrue(moveCollection.remove(mockMove1));
        assertFalse(moveCollection.contains(mockMove1));
        assertNull(moveCollection.getMoveTo(mockPosition1));
    }

    @Test
    void givenMove_whenCheckContainsMoveTo_thenReturnsCorrectly() {
        moveCollection.add(mockMove1);
        assertTrue(moveCollection.containsMoveTo(mockPosition1));
        assertFalse(moveCollection.containsMoveTo(mock(Position.class)));
    }

    @Test
    void givenMoves_whenClear_thenCollectionIsEmpty() {
        moveCollection.add(mockMove1);
        moveCollection.add(mockMove2);
        moveCollection.clear();
        assertTrue(moveCollection.isEmpty());
        assertNull(moveCollection.getMoveTo(mockPosition1));
        assertNull(moveCollection.getMoveTo(mockPosition2));
    }

    @Test
    void givenMoveSet_whenAddAll_thenAllMovesContained() {
        Set<Move> moveSet = Set.of(mockMove1, mockMove2);
        assertTrue(moveCollection.addAll(moveSet));
        assertEquals(2, moveCollection.size());
        assertTrue(moveCollection.contains(mockMove1));
        assertTrue(moveCollection.contains(mockMove2));
    }

    @Test
    void givenMoves_whenRemoveAll_thenRemainingMovesCorrect() {
        moveCollection.add(mockMove1);
        moveCollection.add(mockMove2);
        moveCollection.removeAll(Set.of(mockMove1));
        assertFalse(moveCollection.contains(mockMove1));
        assertTrue(moveCollection.contains(mockMove2));
    }
}
