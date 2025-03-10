package de.schoenfeld.chess.board;

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

class MapChessBoardTest {
    private MapChessBoard chessBoard;
    private ChessPiece whitePawn;
    private ChessPiece blackKnight;
    private Position pawnPosition;
    private Position knightPosition;

    @BeforeEach
    void setup() {
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
    void givenEmptyBoard_whenGetPieceAt_thenReturnNull() {
        assertNull(chessBoard.getPieceAt(new Position(3, 3)));
    }

    @Test
    void givenPieceAdded_whenGetPieceAt_thenReturnCorrectPiece() {
        MapChessBoard newBoard = chessBoard.withPieceAt(whitePawn, pawnPosition);
        assertEquals(whitePawn, newBoard.getPieceAt(pawnPosition));
        assertNull(chessBoard.getPieceAt(pawnPosition)); // Original remains unchanged
    }

    @Test
    void givenPieceAdded_whenGetPiecePosition_thenReturnCorrectPosition() {
        MapChessBoard newBoard = chessBoard.withPieceAt(blackKnight, knightPosition);
        assertEquals(knightPosition, newBoard.getPiecePosition(blackKnight));
        assertNull(chessBoard.getPiecePosition(blackKnight)); // Original unchanged
    }

    @Test
    void givenPieceRemoved_whenGetPieceAt_thenReturnNull() {
        MapChessBoard addedBoard = chessBoard.withPieceAt(whitePawn, pawnPosition);
        MapChessBoard removedBoard = addedBoard.withoutPieceAt(pawnPosition);

        assertNull(removedBoard.getPieceAt(pawnPosition));
        assertEquals(whitePawn, addedBoard.getPieceAt(pawnPosition)); // Previous version intact
    }

    @Test
    void givenMultiplePieces_whenGetPieces_thenReturnCorrectList() {
        MapChessBoard modifiedBoard = chessBoard
                .withPieceAt(whitePawn, pawnPosition)
                .withPieceAt(blackKnight, knightPosition);

        List<ChessPiece> whitePieces = modifiedBoard.getPieces(true);
        assertEquals(1, whitePieces.size());
        assertTrue(whitePieces.contains(whitePawn));
    }

    @Test
    void givenMultiplePieces_whenGetPiecesOfType_thenReturnCorrectList() {
        MapChessBoard modifiedBoard = chessBoard
                .withPieceAt(whitePawn, pawnPosition)
                .withPieceAt(blackKnight, knightPosition);

        List<ChessPiece> whitePawns = modifiedBoard.getPiecesOfType(PieceType.PAWN, true);
        assertEquals(1, whitePawns.size());
        assertTrue(whitePawns.contains(whitePawn));
    }

    @Test
    void givenPieceMoved_whenGetPieceAt_thenNewPositionHasPiece() {
        Position from = new Position(0, 0);
        Position to = new Position(4, 4);

        MapChessBoard addedBoard = chessBoard.withPieceAt(whitePawn, from);
        MapChessBoard movedBoard = addedBoard.withPieceMoved(from, to);

        assertNull(movedBoard.getPieceAt(from));
        assertEquals(whitePawn, movedBoard.getPieceAt(to));
    }

    @Test
    void givenInvalidPosition_whenWithPieceAt_thenThrowException() {
        Position invalid = new Position(8, 8);
        assertThrows(IllegalArgumentException.class,
                () -> chessBoard.withPieceAt(whitePawn, invalid));
    }

    @Test
    void givenNewBounds_whenWithBounds_thenMaintainPiecesWithinBounds() {
        ChessBoardBounds newBounds = new ChessBoardBounds(4, 4);
        MapChessBoard smallBoard = chessBoard.withBounds(newBounds);

        assertEquals(newBounds, smallBoard.getBounds());
        assertTrue(smallBoard.getPieces(true).isEmpty());
    }
}