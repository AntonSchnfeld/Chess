package de.schoenfeld.chess.data;

import de.schoenfeld.chess.board.MapChessBoard;
import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapChessBoardTest {
    private MapChessBoard chessBoard;
    private ChessPiece whitePawn;
    private ChessPiece blackKnight;
    private Position pawnPosition;
    private Position knightPosition;

    @BeforeEach
    public void setup() {
        chessBoard = new MapChessBoard(new ChessBoardBounds(8, 8));
        whitePawn = mock(ChessPiece.class);
        blackKnight = mock(ChessPiece.class);
        pawnPosition = new Position(1, 1);
        knightPosition = new Position(2, 2);

        when(whitePawn.isWhite()).thenReturn(true);
        when(blackKnight.isWhite()).thenReturn(false);
        when(whitePawn.getPieceType()).thenReturn(PieceType.PAWN);
        when(blackKnight.getPieceType()).thenReturn(PieceType.KNIGHT);
    }

    @Test
    public void givenEmptyBoard_whenGetPieceAt_thenReturnNull() {
        // Given: An empty board

        // When: Getting a piece at a position
        ChessPiece piece = chessBoard.getPieceAt(new Position(3, 3));

        // Then: The result should be null
        assertNull(piece);
    }

    @Test
    public void givenPieceOnBoard_whenGetPieceAt_thenReturnCorrectPiece() {
        // Given: A piece placed on the board
        chessBoard.setPiece(whitePawn, pawnPosition);

        // When: Getting the piece at the specific position
        ChessPiece piece = chessBoard.getPieceAt(pawnPosition);

        // Then: The returned piece should be the one that was placed
        assertEquals(whitePawn, piece);
    }

    @Test
    public void givenPieceOnBoard_whenGetPiecePosition_thenReturnCorrectPosition() {
        // Given: A piece placed on the board
        chessBoard.setPiece(blackKnight, knightPosition);

        // When: Getting the position of the placed piece
        Position position = chessBoard.getPiecePosition(blackKnight);

        // Then: The returned position should be the correct one
        assertEquals(knightPosition, position);
    }

    @Test
    public void givenPieceOnBoard_whenRemovePieceAt_thenPieceIsRemoved() {
        // Given: A piece placed on the board
        chessBoard.setPiece(whitePawn, pawnPosition);

        // When: Removing the piece at that position
        chessBoard.removePieceAt(pawnPosition);

        // Then: The position should now be empty
        assertNull(chessBoard.getPieceAt(pawnPosition));
    }

    @Test
    public void givenBoardWithPieces_whenGetPieces_thenReturnCorrectList() {
        // Given: Multiple pieces placed on the board
        chessBoard.setPiece(whitePawn, pawnPosition);
        chessBoard.setPiece(blackKnight, knightPosition);

        // When: Retrieving all white pieces
        List<ChessPiece> whitePieces = chessBoard.getPieces(true);

        // Then: Only white pieces should be returned
        assertEquals(1, whitePieces.size());
        assertTrue(whitePieces.contains(whitePawn));
    }

    @Test
    public void givenBoardWithPieces_whenGetPiecesOfType_thenReturnCorrectList() {
        // Given: Multiple pieces of different types on the board
        chessBoard.setPiece(whitePawn, pawnPosition);
        chessBoard.setPiece(blackKnight, knightPosition);

        // When: Retrieving all white pawns
        List<ChessPiece> whitePawns = chessBoard.getPiecesOfType(PieceType.PAWN, true);

        // Then: The correct piece should be returned
        assertEquals(1, whitePawns.size());
        assertTrue(whitePawns.contains(whitePawn));
    }
}
