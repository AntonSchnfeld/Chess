package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;
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
    private Square pawnSquare;
    private Square knightSquare;

    @BeforeEach
    void setup() {
        chessBoard = new MapChessBoard(new ChessBoardBounds(8, 8));
        whitePawn = mock(ChessPiece.class);
        blackKnight = mock(ChessPiece.class);
        pawnSquare = Square.of(1, 1);
        knightSquare = Square.of(2, 2);

        when(whitePawn.isWhite()).thenReturn(true);
        when(blackKnight.isWhite()).thenReturn(false);
        when(whitePawn.pieceType()).thenReturn(PieceType.PAWN);
        when(blackKnight.pieceType()).thenReturn(PieceType.KNIGHT);
    }

    @Test
    void givenEmptyBoard_whenGetPieceAt_thenReturnNull() {
        assertNull(chessBoard.getPieceAt(Square.of(3, 3)));
    }

    @Test
    void givenPieceAdded_whenGetPieceAt_thenReturnCorrectPiece() {
        MapChessBoard newBoard = chessBoard.withPieceAt(whitePawn, pawnSquare);
        assertEquals(whitePawn, newBoard.getPieceAt(pawnSquare));
        assertNull(chessBoard.getPieceAt(pawnSquare)); // Original remains unchanged
    }

    @Test
    void givenPieceAdded_whenGetPiecePosition_thenReturnCorrectPosition() {
        MapChessBoard newBoard = chessBoard.withPieceAt(blackKnight, knightSquare);
        assertEquals(knightSquare, newBoard.getPiecePosition(blackKnight));
        assertNull(chessBoard.getPiecePosition(blackKnight)); // Original unchanged
    }

    @Test
    void givenPieceRemoved_whenGetPieceAt_thenReturnNull() {
        MapChessBoard addedBoard = chessBoard.withPieceAt(whitePawn, pawnSquare);
        MapChessBoard removedBoard = addedBoard.withoutPieceAt(pawnSquare);

        assertNull(removedBoard.getPieceAt(pawnSquare));
        assertEquals(whitePawn, addedBoard.getPieceAt(pawnSquare)); // Previous version intact
    }

    @Test
    void givenMultiplePieces_whenGetPieces_OfColour_thenReturnCorrectList() {
        MapChessBoard modifiedBoard = chessBoard
                .withPieceAt(whitePawn, pawnSquare)
                .withPieceAt(blackKnight, knightSquare);

        List<ChessPiece> whitePieces = modifiedBoard.getPiecesOfColour(true);
        assertEquals(1, whitePieces.size());
        assertTrue(whitePieces.contains(whitePawn));
    }

    @Test
    void givenMultiplePieces_whenGetPiecesOfColourOfType_thenReturnCorrectList() {
        MapChessBoard modifiedBoard = chessBoard
                .withPieceAt(whitePawn, pawnSquare)
                .withPieceAt(blackKnight, knightSquare);

        List<ChessPiece> whitePawns = modifiedBoard.getPiecesOfTypeAndColour(PieceType.PAWN, true);
        assertEquals(1, whitePawns.size());
        assertTrue(whitePawns.contains(whitePawn));
    }

    @Test
    void givenPieceMoved_whenGetPieceAt_thenNewPositionHasPiece() {
        Square from = Square.of(0, 0);
        Square to = Square.of(4, 4);

        MapChessBoard addedBoard = chessBoard.withPieceAt(whitePawn, from);
        MapChessBoard movedBoard = addedBoard.withPieceMoved(from, to);

        assertNull(movedBoard.getPieceAt(from));
        assertEquals(whitePawn, movedBoard.getPieceAt(to));
    }

    @Test
    void givenInvalidPosition_whenWithPieceAt_thenThrowException() {
        Square invalid = Square.of(8, 8);
        assertThrows(IllegalArgumentException.class,
                () -> chessBoard.withPieceAt(whitePawn, invalid));
    }

    @Test
    void givenNewBounds_whenWithBounds_thenMaintainPiecesWithinBounds() {
        ChessBoardBounds newBounds = new ChessBoardBounds(4, 4);
        MapChessBoard smallBoard = chessBoard.withBounds(newBounds);

        assertEquals(newBounds, smallBoard.getBounds());
        assertTrue(smallBoard.getPiecesOfColour(true).isEmpty());
    }
}