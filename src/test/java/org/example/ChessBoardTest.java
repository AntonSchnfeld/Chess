package org.example;

import org.example.pieces.ChessPiece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class ChessBoardTest {
    private ChessBoard chessBoard;
    private ChessBoardBounds mockDimensions;
    private ChessPiece mockPiece;
    private Position startPosition;

    @BeforeEach
    void setUp() {
        mockDimensions = mock(ChessBoardBounds.class);
        mockPiece = mock(ChessPiece.class);
        startPosition = mock(Position.class);
        chessBoard = new ChessBoard(mockDimensions);
    }

    @Test
    void givenPosition_whenGetPieceAt_thenReturnCorrectPiece() {
        // Arrange: Place the mock piece on the board
        chessBoard.putChessPiece(startPosition, mockPiece);

        // Act: Retrieve the piece at the start position
        ChessPiece piece = chessBoard.getPieceAt(startPosition);

        // Assert: The piece should match the mock piece
        assertEquals(mockPiece, piece);
    }

    @Test
    void givenChessBoard_whenGetChessBoardDimensions_thenReturnCorrectBounds() {
        // Act: Retrieve the chess board dimensions
        ChessBoardBounds dimensions = chessBoard.getChessBoardBounds();

        // Assert: The dimensions should match the mock dimensions
        assertEquals(mockDimensions, dimensions);
    }
}
