package de.schoenfeld.chess.move;

import de.schoenfeld.chess.model.*;
import de.schoenfeld.chess.move.components.MoveComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoveTest {
    private GameState<StandardPieceType> gameState;
    private ChessPiece<StandardPieceType> piece;
    private Square from;
    private Square to;
    private MoveComponent<StandardPieceType> mockComponent;
    private Move<StandardPieceType> tested;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        gameState = new GameState<>();
        piece = new ChessPiece<>(StandardPieceType.PAWN, true);
        from = Square.a1;
        to = Square.b1;
        gameState.setPieceAt(piece, from);
        mockComponent = mock(MoveComponent.class);
    }

    @Test
    public void givenValidNormalMove_whenExecuteOn_thenExecuteProperly() {
        tested = Move.of(piece, from, to);

        tested.executeOn(gameState);

        assertFalse(gameState.isWhiteTurn());
        assertEquals(piece, gameState.getPieceAt(to));
        assertEquals(1, gameState.getMoveHistory().getMoveCount());
        assertEquals(tested, gameState.getMoveHistory().getLastMove());
    }

    @Test
    public void givenValidMoveWithComponent_whenExecuteOn_thenExecuteProperly() {
        tested = Move.of(piece, from, to, mockComponent);

        tested.executeOn(gameState);

        assertFalse(gameState.isWhiteTurn());
        assertEquals(piece, gameState.getPieceAt(to));
        assertEquals(1, gameState.getMoveHistory().getMoveCount());
        assertEquals(tested, gameState.getMoveHistory().getLastMove());
        verify(mockComponent).executeOn(gameState, tested);
    }

    @Test
    public void givenValidNormalMove_whenUndoOn_thenUndoesProperly() {
        tested = Move.of(piece, from, to);

        tested.executeOn(gameState);
        tested.undoOn(gameState);

        assertTrue(gameState.isWhiteTurn());
        assertEquals(piece, gameState.getPieceAt(from));
        assertEquals(0, gameState.getMoveHistory().getMoveCount());
    }

    @Test
    public void givenValidMoveWithComponent_whenExecuteOn_thenUndoesProperly() {
        tested = Move.of(piece, from, to, mockComponent);

        tested.executeOn(gameState);
        tested.undoOn(gameState);

        assertTrue(gameState.isWhiteTurn());
        assertEquals(piece, gameState.getPieceAt(from));
        assertEquals(0, gameState.getMoveHistory().getMoveCount());
        verify(mockComponent).undoOn(gameState, tested);
    }
}
