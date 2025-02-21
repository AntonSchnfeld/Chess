package org.example.pieces;

import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class BishopTest {

    private ChessBoardView mockView;
    private Bishop bishop;
    private Position bishopPosition;

    @BeforeEach
    public void setUp() {
        mockView = mock(ChessBoardView.class);
        ChessBoardBounds bounds = new ChessBoardBounds(8, 8); // Default to 8x8 chessboard
        bishop = new Bishop(true);
        bishopPosition = new Position(3, 3);

        when(mockView.getChessBoardBounds()).thenReturn(bounds);
        when(mockView.getPieceAt(bishopPosition)).thenReturn(bishop);
    }

    @Test
    public void givenEmptyChessBoardView_whenGetValidMoves_thenReturnExpectedMoves() {
        when(mockView.getPieceAt(any())).thenReturn(null);

        MoveCollection moves = bishop.getValidMoves(mockView, bishopPosition);

        for (int x = 4, y = 4; x < 8; x++, y++) {
            Assertions.assertTrue(moves.containsMoveTo(new Position(x, y)));
        }
        for (int x = 0, y = 0; x < 3; x++, y++) {
            Assertions.assertTrue(moves.containsMoveTo(new Position(x, y)));
        }
        for (int x = 2, y = 4; -1 < x; x--, y++) {
            Assertions.assertTrue(moves.containsMoveTo(new Position(x, y)));
        }
        for (int x = 4, y = 2; -1 < y; x++, y--) {
            Assertions.assertTrue(moves.containsMoveTo(new Position(x, y)));
        }

        Assertions.assertFalse(moves.containsMoveTo(new Position(3, 3)));
        Assertions.assertEquals(13, moves.size());
    }

    @Test
    public void givenFullChessBoardView_whenGetValidMoves_thenReturnEmptyList() {
        ChessPiece mockFriendlyPiece = mock(ChessPiece.class);
        when(mockView.getPieceAt(any())).thenReturn(mockFriendlyPiece);
        when(mockFriendlyPiece.getColour()).thenReturn(true); // Friendly piece

        MoveCollection moves = bishop.getValidMoves(mockView, bishopPosition);

        Assertions.assertTrue(moves.isEmpty());
    }

    @Test
    public void given1By1ChessBoardView_whenGetValidMoves_thenReturnEmptyList() {
        ChessBoardBounds tinyBounds = new ChessBoardBounds(1, 1);
        when(mockView.getChessBoardBounds()).thenReturn(tinyBounds);

        MoveCollection moves = bishop.getValidMoves(mockView, bishopPosition);

        Assertions.assertTrue(moves.isEmpty());
    }

    @Test
    public void givenBishopInEdgeAndBlockedByFriendly_whenGetValidMoves_thenReturnEmptyList() {
        ChessPiece mockFriendly = mock(ChessPiece.class);
        Position friendlyPosition = new Position(1, 1);
        bishopPosition = new Position(0, 0);

        when(mockView.getPieceAt(friendlyPosition)).thenReturn(mockFriendly);
        when(mockFriendly.getColour()).thenReturn(true);

        MoveCollection moves = bishop.getValidMoves(mockView, bishopPosition);

        Assertions.assertTrue(moves.isEmpty());
    }

    @Test
    public void givenBishopInEdgeAndBlockedByHostile_whenGetValidMoves_thenReturnCapture() {
        ChessPiece mockHostile = mock(ChessPiece.class);
        Position hostilePosition = new Position(1, 1);
        bishopPosition = new Position(0, 0);
        when(mockHostile.getColour()).thenReturn(false);

        when(mockView.getPieceAt(hostilePosition)).thenReturn(mockHostile);

        MoveCollection moves = bishop.getValidMoves(mockView, bishopPosition);

        Assertions.assertEquals(1, moves.size());
        Assertions.assertTrue(moves.contains(Move.of(bishopPosition, hostilePosition, bishop, mockHostile, false)));
    }

    @Test
    public void givenFriendlyOnOneSideAndHostileOnOtherSides_whenGetValidMoves_thenReturnCaptureAndFreeDiagonals() {
        // Setup for an 8x8 chessboard
        ChessPiece friendlyPiece = mock(ChessPiece.class);
        ChessPiece hostilePiece = mock(ChessPiece.class);
        when(friendlyPiece.getColour()).thenReturn(true);  // Friendly piece
        when(hostilePiece.getColour()).thenReturn(false); // Hostile piece

        // Set up the board with pieces
        when(mockView.getPieceAt(new Position(2, 2))).thenReturn(friendlyPiece);
        when(mockView.getPieceAt(new Position(2, 4))).thenReturn(hostilePiece);

        MoveCollection moves = bishop.getValidMoves(mockView, bishopPosition);

        // Assertions for blocked, capturable, and free diagonals
        Assertions.assertFalse(moves.contains(Move.normal(bishopPosition, bishopPosition, bishop)));
        Assertions.assertFalse(moves.containsMoveTo(new Position(2, 2))); // Blocked by friendly
        Assertions.assertTrue(moves.containsMoveTo(new Position(2, 4)));  // Capturable hostile
        Assertions.assertFalse(moves.containsMoveTo(new Position(1, 5))); // Beyond hostile
        Assertions.assertTrue(moves.containsMoveTo(new Position(4, 2)));  // Free path
        Assertions.assertTrue(moves.containsMoveTo(new Position(5, 1)));
        Assertions.assertTrue(moves.containsMoveTo(new Position(6, 0)));
        Assertions.assertTrue(moves.containsMoveTo(new Position(4, 4)));  // Free path
        Assertions.assertTrue(moves.containsMoveTo(new Position(5, 5)));
        Assertions.assertTrue(moves.containsMoveTo(new Position(6, 6)));
        Assertions.assertTrue(moves.containsMoveTo(new Position(7, 7)));

        Assertions.assertEquals(8, moves.size());
    }
}
