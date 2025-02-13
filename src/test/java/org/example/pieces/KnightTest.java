package org.example.pieces;

import org.example.ChessBoardBounds;
import org.example.ChessBoardView;
import org.example.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class KnightTest {

    @Test
    public void givenEmptyBoard_whenGetValidMoves_thenAllMovesReturned() {
        Knight knight = new Knight(true);
        ChessBoardView mockView = mock(ChessBoardView.class);
        Position knightPos = new Position(3, 3);

        // Mock board size (8x8)
        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);
        when(mockView.getChessBoardDimensions()).thenReturn(bounds);
        when(mockView.getPieceAt(any())).thenReturn(null); // No pieces on board

        List<Position> moves = knight.getValidMoves(mockView, knightPos);

        Assertions.assertEquals(8, moves.size()); // All 8 moves should be valid
        Assertions.assertTrue(moves.contains(new Position(4, 5)));
        Assertions.assertTrue(moves.contains(new Position(5, 4)));
        Assertions.assertTrue(moves.contains(new Position(2, 5)));
        Assertions.assertTrue(moves.contains(new Position(5, 2)));
        Assertions.assertTrue(moves.contains(new Position(4, 1)));
        Assertions.assertTrue(moves.contains(new Position(1, 4)));
        Assertions.assertTrue(moves.contains(new Position(2, 1)));
        Assertions.assertTrue(moves.contains(new Position(1, 2)));
    }

    @Test
    public void givenFriendlyPieceBlockingMove_whenGetValidMoves_thenMoveIsIgnored() {
        Knight knight = new Knight(true);
        ChessBoardView mockView = mock(ChessBoardView.class);
        Position knightPos = new Position(3, 3);

        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);
        when(mockView.getChessBoardDimensions()).thenReturn(bounds);
        when(mockView.getPieceAt(any())).thenReturn(null);

        // Block position (4,5) with a friendly piece
        ChessPiece friendlyPiece = mock(ChessPiece.class);
        when(friendlyPiece.getColour()).thenReturn(true);
        when(mockView.getPieceAt(new Position(4, 5))).thenReturn(friendlyPiece);

        List<Position> moves = knight.getValidMoves(mockView, knightPos);

        Assertions.assertFalse(moves.contains(new Position(4, 5))); // Blocked by friendly piece
        Assertions.assertEquals(7, moves.size()); // Only 7 valid moves now
    }

    @Test
    public void givenEnemyPiece_whenGetValidMoves_thenMoveIsIncluded() {
        Knight knight = new Knight(true);
        ChessBoardView mockView = mock(ChessBoardView.class);
        Position knightPos = new Position(3, 3);

        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);
        when(mockView.getChessBoardDimensions()).thenReturn(bounds);
        when(mockView.getPieceAt(any())).thenReturn(null);

        // Enemy piece at (4,5)
        ChessPiece enemyPiece = mock(ChessPiece.class);
        when(enemyPiece.getColour()).thenReturn(false);
        when(mockView.getPieceAt(new Position(4, 5))).thenReturn(enemyPiece);

        List<Position> moves = knight.getValidMoves(mockView, knightPos);

        Assertions.assertTrue(moves.contains(new Position(4, 5))); // Should be able to capture enemy piece
        Assertions.assertEquals(8, moves.size()); // All 8 moves should be available
    }

    @Test
    public void givenKnightAtEdge_whenGetValidMoves_thenOutOfBoundsMovesIgnored() {
        Knight knight = new Knight(true);
        ChessBoardView mockView = mock(ChessBoardView.class);
        Position knightPos = new Position(0, 0); // Top-left corner

        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);
        when(mockView.getChessBoardDimensions()).thenReturn(bounds);
        when(mockView.getPieceAt(any())).thenReturn(null);

        List<Position> moves = knight.getValidMoves(mockView, knightPos);

        Assertions.assertEquals(2, moves.size()); // Only two moves possible
        Assertions.assertTrue(moves.contains(new Position(1, 2)));
        Assertions.assertTrue(moves.contains(new Position(2, 1)));
    }

    @Test
    public void givenNullPosition_whenGetValidMoves_thenThrowsException() {
        Knight knight = new Knight(true);
        ChessBoardView mockView = mock(ChessBoardView.class);

        Assertions.assertThrows(NullPointerException.class, () -> knight.getValidMoves(mockView, null));
    }
}