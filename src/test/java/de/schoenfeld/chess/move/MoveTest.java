package de.schoenfeld.chess.move;

import de.schoenfeld.chess.board.BoardUtility;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.components.MoveComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        gameState.setPieceAt(from, piece);
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

    @Test
    public void givenValidMoveWithComponent_whenUndoOn_thenUndoesProperly() {
        gameState = new GameState<>(BoardUtility.getDefaultBoard());
        piece = new ChessPiece<>(StandardPieceType.PAWN, true);
        from = Square.of(4, 1);
        to = Square.of(4, 2);
        tested = Move.of(piece, from, to, mockComponent);

        tested.executeOn(gameState);

        assertFalse(gameState.isWhiteTurn());
        assertEquals(piece, gameState.getPieceAt(to));
        assertNull(gameState.getPieceAt(from));
        assertEquals(1, gameState.getMoveHistory().getMoveCount());
        assertEquals(tested, gameState.getMoveHistory().getLastMove());
        verify(mockComponent).executeOn(gameState, tested);

        tested.undoOn(gameState);

        assertTrue(gameState.isWhiteTurn());
        assertEquals(piece, gameState.getPieceAt(from));
        assertNull(gameState.getPieceAt(to));
        assertEquals(0, gameState.getMoveHistory().getMoveCount());
        assertNull(gameState.getMoveHistory().getLastMove());
        verify(mockComponent).undoOn(gameState, tested);
    }

    public void givenAFewMoves_whenExecutingAndUndoing_thenWorksProperly() {

    }
}
