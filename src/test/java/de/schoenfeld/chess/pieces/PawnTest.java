package de.schoenfeld.chess.pieces;

import de.schoenfeld.chess.ChessBoardBounds;
import de.schoenfeld.chess.ChessBoardView;
import de.schoenfeld.chess.MoveCollection;
import de.schoenfeld.chess.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PawnTest {
    private Pawn pawn;
    private ChessBoardView mockView;

    @BeforeEach
    public void setUp() {
        pawn = new Pawn(true);
        mockView = mock(ChessBoardView.class);
        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);

        when(mockView.getChessBoardBounds()).thenReturn(bounds);
    }

    @Test
    public void givenEmptyBoardAndNotMoved_whenGetValidMoves_thenCanAdvanceTwoOrOneSquares() {
        Position pawnPosition = new Position(0, 1);
        when(mockView.getPieceAt(pawnPosition)).thenReturn(pawn);

        MoveCollection moves = pawn.getValidMoves(mockView, pawnPosition);

        assertEquals(2, moves.size());
        assertTrue(moves.containsMoveTo(new Position(0, 2)));
        assertTrue(moves.containsMoveTo(new Position(0, 3)));
    }

    @Test
    public void givenHostilesOnDiagonalsAndNotMoved_whenGetValidMoves_thenCanCaptureOrAdvance() {
        Position pawnPosition = new Position(1, 1);
        ChessPiece mockHostile = mock(ChessPiece.class);
        Position leftHostilePosition = new Position(0, 2);
        Position rightHostilePosition = new Position(2, 2);

        when(mockHostile.getColour()).thenReturn(false);
        when(mockView.getPieceAt(pawnPosition)).thenReturn(pawn);
        when(mockView.getPieceAt(leftHostilePosition)).thenReturn(mockHostile);
        when(mockView.getPieceAt(rightHostilePosition)).thenReturn(mockHostile);

        MoveCollection moves = pawn.getValidMoves(mockView, pawnPosition);

        assertEquals(4, moves.size());
        assertTrue(moves.containsMoveTo(leftHostilePosition));
        assertTrue(moves.containsMoveTo(rightHostilePosition));
        assertEquals(mockHostile, moves.getMoveTo(leftHostilePosition).capturedPiece());
        assertEquals(mockHostile, moves.getMoveTo(rightHostilePosition).capturedPiece());
        assertTrue(moves.containsMoveTo(new Position(1, 3)));
        assertTrue(moves.containsMoveTo(new Position(1, 2)));
    }

    @Test
    public void givenFriendlyTwoSquaresInFront_whenGetValidMoves_thenCanAdvanceOneSquare() {
        Position pawnPosition = new Position(1, 1);
        ChessPiece mockFriendly = mock(ChessPiece.class);
        Position friendlyPosition = new Position(1, 3);

        when(mockFriendly.getColour()).thenReturn(true);
        when(mockView.getPieceAt(pawnPosition)).thenReturn(pawn);
        when(mockView.getPieceAt(friendlyPosition)).thenReturn(mockFriendly);

        MoveCollection moves = pawn.getValidMoves(mockView, pawnPosition);

        assertEquals(1, moves.size());
        assertTrue(moves.containsMoveTo(new Position(1, 2)));
    }

    @Test
    public void givenFriendlyInFront_whenGetValidMoves_thenCantMove() {
        Position pawnPosition = new Position(1, 1);
        ChessPiece mockFriendly = mock(ChessPiece.class);
        Position friendlyPosition = new Position(1, 2);

        when(mockFriendly.getColour()).thenReturn(true);
        when(mockView.getPieceAt(pawnPosition)).thenReturn(pawn);
        when(mockView.getPieceAt(friendlyPosition)).thenReturn(mockFriendly);

        MoveCollection moves = pawn.getValidMoves(mockView, pawnPosition);

        assertTrue(moves.isEmpty());
    }

    @Test
    public void givenHostileInFront_whenGetValidMoves_thenCantMove() {
        Position pawnPosition = new Position(1, 1);
        ChessPiece mockHostile = mock(ChessPiece.class);
        Position hostilePosition = new Position(1, 2);

        when(mockHostile.getColour()).thenReturn(false);
        when(mockView.getPieceAt(pawnPosition)).thenReturn(pawn);
        when(mockView.getPieceAt(hostilePosition)).thenReturn(mockHostile);

        MoveCollection moves = pawn.getValidMoves(mockView, pawnPosition);

        assertTrue(moves.isEmpty());
    }

    @Test
    public void givenSmallChessBoard_whenGetValidMoves_thenCantMove() {
        ChessBoardView mockTinyView = mock(ChessBoardView.class);
        Position pawnPosition = new Position(0, 0);
        ChessBoardBounds tinyBounds = new ChessBoardBounds(1, 1);

        when(mockTinyView.getChessBoardBounds()).thenReturn(tinyBounds);
        when(mockTinyView.getPieceAt(pawnPosition)).thenReturn(pawn);

        MoveCollection moves = pawn.getValidMoves(mockTinyView, pawnPosition);

        assertTrue(moves.isEmpty());
    }
}
