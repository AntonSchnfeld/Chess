package org.example;

import org.example.pieces.ChessPiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class ChessBoardTest {

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
