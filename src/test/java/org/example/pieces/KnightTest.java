package org.example.pieces;

import org.example.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class KnightTest {
    private Knight knight;
    private ChessBoardView mockView;

    @BeforeEach
    public void setUp() {
        knight = new Knight(true);
        mockView = mock(ChessBoardView.class);
        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);
        when(mockView.getChessBoardBounds()).thenReturn(bounds);
        when(mockView.getPieceAt(any())).thenReturn(null);
    }

    @Test
    public void givenEmptyBoard_whenGetValidMoves_thenAllMovesReturned() {
        Position knightPos = new Position(3, 3);
        MoveCollection moves = knight.getValidMoves(mockView, knightPos);

        Assertions.assertEquals(8, moves.size());
        Assertions.assertTrue(moves.contains(Move.of(knightPos, new Position(4, 5), knight, null, false)));
        Assertions.assertTrue(moves.contains(Move.of(knightPos, new Position(5, 4), knight, null, false)));
        Assertions.assertTrue(moves.contains(Move.of(knightPos, new Position(2, 5), knight, null, false)));
        Assertions.assertTrue(moves.contains(Move.of(knightPos, new Position(5, 2), knight, null, false)));
        Assertions.assertTrue(moves.contains(Move.of(knightPos, new Position(4, 1), knight, null, false)));
        Assertions.assertTrue(moves.contains(Move.of(knightPos, new Position(1, 4), knight, null, false)));
        Assertions.assertTrue(moves.contains(Move.of(knightPos, new Position(2, 1), knight, null, false)));
        Assertions.assertTrue(moves.contains(Move.of(knightPos, new Position(1, 2), knight, null, false)));
    }

    @Test
    public void givenFriendlyPieceBlockingMove_whenGetValidMoves_thenMoveIsIgnored() {
        Position knightPos = new Position(3, 3);
        ChessPiece friendlyPiece = mock(ChessPiece.class);
        when(friendlyPiece.getColour()).thenReturn(true);
        when(mockView.getPieceAt(new Position(4, 5))).thenReturn(friendlyPiece);

        MoveCollection moves = knight.getValidMoves(mockView, knightPos);

        Assertions.assertFalse(moves.contains(Move.of(knightPos, new Position(4, 5), knight, null, false)));
        Assertions.assertEquals(7, moves.size());
    }

    @Test
    public void givenEnemyPiece_whenGetValidMoves_thenMoveIsIncluded() {
        Position knightPos = new Position(3, 3);
        ChessPiece enemyPiece = mock(ChessPiece.class);
        when(enemyPiece.getColour()).thenReturn(false);
        when(mockView.getPieceAt(new Position(4, 5))).thenReturn(enemyPiece);

        MoveCollection moves = knight.getValidMoves(mockView, knightPos);

        Assertions.assertTrue(moves.contains(Move.of(knightPos, new Position(4, 5), knight, enemyPiece, false)));
        Assertions.assertEquals(8, moves.size());
    }

    @Test
    public void givenKnightAtEdge_whenGetValidMoves_thenOutOfBoundsMovesIgnored() {
        Position knightPos = new Position(0, 0);
        MoveCollection moves = knight.getValidMoves(mockView, knightPos);

        Assertions.assertEquals(2, moves.size());
        Assertions.assertTrue(moves.contains(Move.of(knightPos, new Position(1, 2), knight, null, false)));
        Assertions.assertTrue(moves.contains(Move.of(knightPos, new Position(2, 1), knight, null, false)));
    }

    @Test
    public void givenNullPosition_whenGetValidMoves_thenThrowsException() {
        Assertions.assertThrows(NullPointerException.class, () -> knight.getValidMoves(mockView, null));
    }
}
