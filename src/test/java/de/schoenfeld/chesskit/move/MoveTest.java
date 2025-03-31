package de.schoenfeld.chesskit.move;

import de.schoenfeld.chesskit.board.BoardUtility;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.Square;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.components.MoveComponent;
import de.schoenfeld.chesskit.rules.Rules;
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
        gameState = new GameState<>(Rules.standard());
        piece = new ChessPiece<>(StandardPieceType.PAWN, true);
        from = Square.a1;
        to = Square.b1;
        gameState.setPieceAt(from, piece);
        mockComponent = mock(MoveComponent.class);
    }

    @Test
    public void givenValidNormalMove_whenExecuteOn_thenExecuteProperly() {
        tested = Move.claim(piece, from, to);

        gameState.makeMove(tested);

        assertFalse(gameState.isWhiteTurn());
        assertEquals(piece, gameState.getPieceAt(to));
        assertEquals(1, gameState.getMoveHistory().getMoveCount());
        assertEquals(tested, gameState.getMoveHistory().getLastMove());
    }

    @Test
    public void givenValidMoveWithComponent_whenExecuteOn_thenExecuteProperly() {
        tested = Move.claim(piece, from, to, mockComponent);

        gameState.makeMove(tested);

        assertFalse(gameState.isWhiteTurn());
        assertEquals(piece, gameState.getPieceAt(to));
        assertEquals(1, gameState.getMoveHistory().getMoveCount());
        assertEquals(tested, gameState.getMoveHistory().getLastMove());
        verify(mockComponent).makeOn(gameState, tested);
    }

    @Test
    public void givenValidNormalMove_whenUndoOn_thenUndoesProperly() {
        tested = Move.claim(piece, from, to);

        gameState.makeMove(tested);
        gameState.unmakeLastMove();

        assertTrue(gameState.isWhiteTurn());
        assertEquals(piece, gameState.getPieceAt(from));
        assertEquals(0, gameState.getMoveHistory().getMoveCount());
    }

    @Test
    public void givenValidMoveWithComponent_whenExecuteOn_thenUndoesProperly() {
        tested = Move.claim(piece, from, to, mockComponent);

        gameState.makeMove(tested);
        gameState.unmakeLastMove();

        assertTrue(gameState.isWhiteTurn());
        assertEquals(piece, gameState.getPieceAt(from));
        assertEquals(0, gameState.getMoveHistory().getMoveCount());
        verify(mockComponent).unmakeOn(gameState, tested);
    }

    @Test
    public void givenValidMoveWithComponent_whenUndoOn_thenUndoesProperly() {
        gameState = new GameState<>(BoardUtility.getDefaultBoard(), Rules.standard());
        piece = new ChessPiece<>(StandardPieceType.PAWN, true);
        from = Square.of(4, 1);
        to = Square.of(4, 2);
        tested = Move.claim(piece, from, to, mockComponent);

        gameState.makeMove(tested);

        assertFalse(gameState.isWhiteTurn());
        assertEquals(piece, gameState.getPieceAt(to));
        assertNull(gameState.getPieceAt(from));
        assertEquals(1, gameState.getMoveHistory().getMoveCount());
        assertEquals(tested, gameState.getMoveHistory().getLastMove());
        verify(mockComponent).makeOn(gameState, tested);

        gameState.unmakeLastMove();

        assertTrue(gameState.isWhiteTurn());
        assertEquals(piece, gameState.getPieceAt(from));
        assertNull(gameState.getPieceAt(to));
        assertEquals(0, gameState.getMoveHistory().getMoveCount());
        assertNull(gameState.getMoveHistory().getLastMove());
        verify(mockComponent).unmakeOn(gameState, tested);
    }

    public void givenAFewMoves_whenExecutingAndUndoing_thenWorksProperly() {

    }
}
