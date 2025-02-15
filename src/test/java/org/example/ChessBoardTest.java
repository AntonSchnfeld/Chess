package org.example;

import org.example.pieces.ChessPiece;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChessBoardTest {

    @Test
    void givenValidMove_whenMovePiece_thenMoveIsSuccessful() {
        // Arrange: Mock dependencies
        ChessBoardBounds mockDimensions = mock(ChessBoardBounds.class);
        ChessPiece mockPiece = mock(ChessPiece.class);
        Position startPosition = mock(Position.class);
        Position targetPosition = mock(Position.class);

        // Set up the board with the mock piece using putChessPiece()
        ChessBoard chessBoard = new ChessBoard(mockDimensions);
        chessBoard.putChessPiece(startPosition, mockPiece);

        // Mock the piece's valid moves
        when(mockPiece.getValidMoves(chessBoard, startPosition)).thenReturn(Collections.singletonList(targetPosition));

        // Act: Try to move the piece
        boolean result = chessBoard.movePiece(startPosition, targetPosition);

        // Assert: The move should be successful
        assertTrue(result);
        verify(mockPiece, times(1)).getValidMoves(chessBoard, startPosition);
    }

    @Test
    void givenInvalidMove_whenMovePiece_thenMoveFails() {
        // Arrange: Mock dependencies
        ChessBoardBounds mockDimensions = mock(ChessBoardBounds.class);
        ChessPiece mockPiece = mock(ChessPiece.class);
        Position startPosition = mock(Position.class);
        Position targetPosition = mock(Position.class);

        // Set up the board with the mock piece using putChessPiece()
        ChessBoard chessBoard = new ChessBoard(mockDimensions);
        chessBoard.putChessPiece(startPosition, mockPiece);

        // Mock the piece's valid moves to not contain the target position
        when(mockPiece.getValidMoves(chessBoard, startPosition)).thenReturn(Collections.emptyList());

        // Act: Try to move the piece
        boolean result = chessBoard.movePiece(startPosition, targetPosition);

        // Assert: The move should fail
        assertFalse(result);
        verify(mockPiece, times(1)).getValidMoves(chessBoard, startPosition);
    }

    @Test
    void givenPosition_whenGetPieceAt_thenReturnCorrectPiece() {
        // Arrange: Mock dependencies
        ChessBoardBounds mockDimensions = mock(ChessBoardBounds.class);
        ChessPiece mockPiece = mock(ChessPiece.class);
        Position startPosition = mock(Position.class);

        // Set up the board with the mock piece using putChessPiece()
        ChessBoard chessBoard = new ChessBoard(mockDimensions);
        chessBoard.putChessPiece(startPosition, mockPiece);

        // Act: Retrieve the piece at the start position
        ChessPiece piece = chessBoard.getPieceAt(startPosition);

        // Assert: The piece should match the mock piece
        assertEquals(mockPiece, piece);
    }

    @Test
    void givenChessBoard_whenGetChessBoardDimensions_thenReturnCorrectBounds() {
        // Arrange: Mock dependencies
        ChessBoardBounds mockDimensions = mock(ChessBoardBounds.class);
        ChessPiece mockPiece = mock(ChessPiece.class);
        Position startPosition = mock(Position.class);

        // Set up the board with the mock piece using putChessPiece()
        ChessBoard chessBoard = new ChessBoard(mockDimensions);
        chessBoard.putChessPiece(startPosition, mockPiece);

        // Act: Retrieve the chess board dimensions
        ChessBoardBounds dimensions = chessBoard.getChessBoardBounds();

        // Assert: The dimensions should match the mock dimensions
        assertEquals(mockDimensions, dimensions);
    }
}
