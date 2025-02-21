package org.example.pieces;

import org.example.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class BishopTest {
    @Test
    public void givenEmptyChessBoardView_whenGetValidMoves_thenReturnExpectedMoves() {
        ChessBoardView mockEmptyView = mock(ChessBoardView.class);
        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);
        Position bishopPosition = new Position(3, 3);
        Bishop bishop = new Bishop(true);

        when(mockEmptyView.getChessBoardBounds()).thenReturn(bounds);
        when(mockEmptyView.getPieceAt(any())).thenReturn(null);
        when(mockEmptyView.getPieceAt(bishopPosition)).thenReturn(bishop);

        MoveCollection moves = bishop.getValidMoves(mockEmptyView, bishopPosition);

        for (int x = 4, y = 4; x < 8; x++, y++)
            Assertions.assertTrue(moves.containsMoveTo(new Position(x, y)));
        for (int x = 0, y = 0; x < 3; x++, y++)
            Assertions.assertTrue(moves.containsMoveTo(new Position(x, y)));

        for (int x = 2, y = 4; -1 < x; x--, y++)
            Assertions.assertTrue(moves.containsMoveTo(new Position(x, y)));
        for (int x = 4, y = 2; -1 < y; x++, y--)
            Assertions.assertTrue(moves.containsMoveTo(new Position(x, y)));

        Assertions.assertFalse(moves.containsMoveTo(new Position(3, 3)));

        Assertions.assertEquals(13, moves.size());
    }

    @Test
    public void givenFullChessBoardView_whenGetValidMoves_thenReturnEmptyList() {
        ChessBoardView mockFullView = mock(ChessBoardView.class);
        ChessPiece mockFriendlyPiece = mock(ChessPiece.class);
        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);
        Bishop bishop = new Bishop(true);
        Position bishopPosition = new Position(3, 3);

        when(mockFullView.getPieceAt(any())).thenReturn(mockFriendlyPiece);
        when(mockFullView.getPieceAt(bishopPosition)).thenReturn(bishop);
        when(mockFullView.getChessBoardBounds()).thenReturn(bounds);
        when(mockFriendlyPiece.getColour()).thenReturn(true);

        MoveCollection moves = bishop.getValidMoves(mockFullView, bishopPosition);

        Assertions.assertTrue(moves.isEmpty());
    }

    @Test
    public void given1By1ChessBoardView_whenGetValidMoves_thenReturnEmptyList() {
        ChessBoardView mockTinyView = mock(ChessBoardView.class);
        ChessBoardBounds bounds = new ChessBoardBounds(1, 1);
        Bishop bishop = new Bishop(true);
        Position bishopPosition = new Position(0, 0);

        when(mockTinyView.getChessBoardBounds()).thenReturn(bounds);
        when(mockTinyView.getPieceAt(bishopPosition)).thenReturn(bishop);

        MoveCollection moves = bishop.getValidMoves(mockTinyView, bishopPosition);

        Assertions.assertTrue(moves.isEmpty());
    }

    @Test
    public void givenBishopInEdgeAndBlockedByFriendly_whenGetValidMoves_thenReturnEmptyList() {
        ChessBoardView mockView = mock(ChessBoardView.class);
        ChessBoardBounds bounds = new ChessBoardBounds(3, 3);
        ChessPiece mockFriendly = mock(ChessPiece.class);
        Bishop bishop = new Bishop(true);
        Position bishopPosition = new Position(0, 0);
        Position friendlyPosition = new Position(1, 1);

        when(mockFriendly.getColour()).thenReturn(true);
        when(mockView.getChessBoardBounds()).thenReturn(bounds);
        when(mockView.getPieceAt(friendlyPosition)).thenReturn(mockFriendly);
        when(mockView.getPieceAt(bishopPosition)).thenReturn(bishop);

        MoveCollection moves = bishop.getValidMoves(mockView, bishopPosition);

        Assertions.assertTrue(moves.isEmpty());
    }

    @Test
    public void givenBishopInEdgeAndBlockedByHostile_whenGetValidMoves_thenReturnCapture() {
        ChessBoardView mockView = mock(ChessBoardView.class);
        ChessBoardBounds bounds = new ChessBoardBounds(3, 3);
        ChessPiece mockHostile = mock(ChessPiece.class);
        Bishop bishop = new Bishop(true);
        Position bishopPosition = new Position(0, 0);
        Position hostilePosition = new Position(1, 1);

        when(mockHostile.getColour()).thenReturn(false);
        when(mockView.getChessBoardBounds()).thenReturn(bounds);
        when(mockView.getPieceAt(hostilePosition)).thenReturn(mockHostile);
        when(mockView.getPieceAt(bishopPosition)).thenReturn(bishop);

        MoveCollection moves = bishop.getValidMoves(mockView, bishopPosition);

        Assertions.assertEquals(1, moves.size());
        Assertions.assertTrue(moves.contains(Move.of(bishopPosition, hostilePosition, bishop, mockHostile, false)));
    }

    @Test
    public void givenFriendlyOnOneSideAndHostileOnOtherSides_whenGetValidMoves_thenReturnCaptureAndFreeDiagonals() {
        // Set up an 8x8 chess board and place the bishop at (3,3)
        ChessBoardView mockView = mock(ChessBoardView.class);
        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);
        Bishop bishop = new Bishop(true);
        Position bishopPosition = new Position(3, 3);

        // Create a friendly piece to block the top-left diagonal at (2,2)
        ChessPiece friendlyPiece = mock(ChessPiece.class);
        when(friendlyPiece.getColour()).thenReturn(true);

        // Create a hostile piece on the top-right diagonal at (2,4)
        ChessPiece hostilePiece = mock(ChessPiece.class);
        when(hostilePiece.getColour()).thenReturn(false);

        // Configure the board view
        when(mockView.getChessBoardBounds()).thenReturn(bounds);
        // By default, all positions are empty
        when(mockView.getPieceAt(any())).thenReturn(null);
        // Place the bishop and the blocking/capture pieces
        when(mockView.getPieceAt(bishopPosition)).thenReturn(bishop);
        when(mockView.getPieceAt(new Position(2, 2))).thenReturn(friendlyPiece);
        when(mockView.getPieceAt(new Position(2, 4))).thenReturn(hostilePiece);

        // Get the valid moves for the bishop
        MoveCollection moves = bishop.getValidMoves(mockView, bishopPosition);

        // The bishop's own position should not be in the valid moves.
        Assertions.assertFalse(moves.contains(Move.normal(bishopPosition, bishopPosition, bishop)));

        // Top-left diagonal: blocked by a friendly piece at (2,2), so no moves are allowed.
        Assertions.assertFalse(moves.containsMoveTo(new Position(2, 2)));

        // Top-right diagonal: the hostile piece at (2,4) should be capturable,
        // and no moves beyond it should be allowed.
        Assertions.assertTrue(moves.containsMoveTo(new Position(2, 4)));
        Assertions.assertFalse(moves.containsMoveTo(new Position(1, 5)));

        // Bottom-left diagonal: free path, so moves should include (4,2), (5,1), (6,0)
        Assertions.assertTrue(moves.containsMoveTo(new Position(4, 2)));
        Assertions.assertTrue(moves.containsMoveTo(new Position(5, 1)));
        Assertions.assertTrue(moves.containsMoveTo(new Position(6, 0)));

        // Bottom-right diagonal: free path, so moves should include (4,4), (5,5), (6,6), (7,7)
        Assertions.assertTrue(moves.containsMoveTo(new Position(4, 4)));
        Assertions.assertTrue(moves.containsMoveTo(new Position(5, 5)));
        Assertions.assertTrue(moves.containsMoveTo(new Position(6, 6)));
        Assertions.assertTrue(moves.containsMoveTo(new Position(7, 7)));

        // Total expected moves:
        // Top-right: 1 capture move
        // Bottom-left: 3 moves
        // Bottom-right: 4 moves
        // => 1 + 3 + 4 = 8 moves
        Assertions.assertEquals(8, moves.size());
    }
}
