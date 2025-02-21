package org.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import java.util.Set;

class MoveCollectionTest {
    @Test
    void givenMove_whenAdd_thenContainsMove() {
        MoveCollection moveCollection = new MoveCollection();
        Move mockMove1 = mock(Move.class);
        Position mockPosition1 = mock(Position.class);
        when(mockMove1.to()).thenReturn(mockPosition1);

        assertTrue(moveCollection.add(mockMove1));
        assertTrue(moveCollection.contains(mockMove1));
        assertEquals(mockMove1, moveCollection.getMoveTo(mockPosition1));
    }

    @Test
    void givenDuplicateMove_whenAdd_thenFails() {
        MoveCollection moveCollection = new MoveCollection();
        Move mockMove1 = mock(Move.class);
        when(mockMove1.to()).thenReturn(mock(Position.class));

        assertTrue(moveCollection.add(mockMove1));
        assertFalse(moveCollection.add(mockMove1)); // Duplicate should not be added
        assertEquals(1, moveCollection.size());
    }

    @Test
    void givenMove_whenRemove_thenNotContained() {
        MoveCollection moveCollection = new MoveCollection();
        Move mockMove1 = mock(Move.class);
        Position mockPosition1 = mock(Position.class);
        when(mockMove1.to()).thenReturn(mockPosition1);

        moveCollection.add(mockMove1);
        assertTrue(moveCollection.remove(mockMove1));
        assertFalse(moveCollection.contains(mockMove1));
        assertNull(moveCollection.getMoveTo(mockPosition1));
    }

    @Test
    void givenMove_whenCheckContainsMoveTo_thenReturnsCorrectly() {
        MoveCollection moveCollection = new MoveCollection();
        Move mockMove1 = mock(Move.class);
        Position mockPosition1 = mock(Position.class);
        when(mockMove1.to()).thenReturn(mockPosition1);

        moveCollection.add(mockMove1);
        assertTrue(moveCollection.containsMoveTo(mockPosition1));
        assertFalse(moveCollection.containsMoveTo(mock(Position.class)));
    }

    @Test
    void givenMoves_whenClear_thenCollectionIsEmpty() {
        MoveCollection moveCollection = new MoveCollection();
        Move mockMove1 = mock(Move.class);
        Move mockMove2 = mock(Move.class);
        Position mockPosition1 = mock(Position.class);
        Position mockPosition2 = mock(Position.class);
        when(mockMove1.to()).thenReturn(mockPosition1);
        when(mockMove2.to()).thenReturn(mockPosition2);

        moveCollection.add(mockMove1);
        moveCollection.add(mockMove2);
        moveCollection.clear();
        assertTrue(moveCollection.isEmpty());
        assertNull(moveCollection.getMoveTo(mockPosition1));
        assertNull(moveCollection.getMoveTo(mockPosition2));
    }

    @Test
    void givenMoveSet_whenAddAll_thenAllMovesContained() {
        MoveCollection moveCollection = new MoveCollection();
        Move mockMove1 = mock(Move.class);
        Move mockMove2 = mock(Move.class);
        when(mockMove1.to()).thenReturn(mock(Position.class));
        when(mockMove2.to()).thenReturn(mock(Position.class));

        Set<Move> moveSet = Set.of(mockMove1, mockMove2);
        assertTrue(moveCollection.addAll(moveSet));
        assertEquals(2, moveCollection.size());
        assertTrue(moveCollection.contains(mockMove1));
        assertTrue(moveCollection.contains(mockMove2));
    }

    @Test
    void givenMoves_whenRemoveAll_thenRemainingMovesCorrect() {
        MoveCollection moveCollection = new MoveCollection();
        Move mockMove1 = mock(Move.class);
        Move mockMove2 = mock(Move.class);
        when(mockMove1.to()).thenReturn(mock(Position.class));
        when(mockMove2.to()).thenReturn(mock(Position.class));

        moveCollection.add(mockMove1);
        moveCollection.add(mockMove2);
        moveCollection.removeAll(Set.of(mockMove1));
        assertFalse(moveCollection.contains(mockMove1));
        assertTrue(moveCollection.contains(mockMove2));
    }
}
