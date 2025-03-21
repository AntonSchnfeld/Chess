package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListChessBoardTest {
    private ListChessBoard<PieceType> tested;
    private ChessBoardBounds bounds;

    @BeforeEach
    public void setUp() {
        bounds = new ChessBoardBounds(8, 8);
        tested = new ListChessBoard<>(bounds);
    }

    @Test
    public void givenNoPieces_whenGetPieceAt_thenNull() {
        // Given
        // Board is already empty
        // When
        ChessPiece piece = tested.getPieceAt(Square.of(0, 0));
        // Then
        assertNull(piece);
    }

    @Test
    public void givenPieceAtPosition_whenGetPieceAt_thenPiece() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        Square pieceSquare = Square.of(0, 0);

        when(piece.isWhite()).thenReturn(false);

        tested = tested.withPieceAt(piece, pieceSquare);
        // When
        ChessPiece result = tested.getPieceAt(pieceSquare);
        // Then
        assertSame(piece, result);
    }

    @Test
    public void givenPieceAtPosition_whenGetPiecePosition_thenPosition() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        Square pieceSquare = Square.of(0, 0);

        when(piece.isWhite()).thenReturn(false);

        tested = tested.withPieceAt(piece, pieceSquare);
        // When
        Square result = tested.getPiecePosition(piece);
        // Then
        assertEquals(pieceSquare, result);
    }

    @Test
    public void givenNoPieces_whenGetPiecePosition_thenNull() {
        // Given
        // Board is already empty
        // When
        Square result = tested.getPiecePosition(mock(ChessPiece.class));
        // Then
        assertNull(result);
    }

    @Test
    public void givenChessBoardBounds_whenGetBounds_thenBounds() {
        // Given
        // When
        ChessBoardBounds result = tested.getBounds();
        // Then
        assertEquals(bounds, result);
    }

    @Test
    public void givenNoPieces_whenGetPiecesOfColour_thenEmptyList() {
        // Given
        // Board is already empty
        // When
        var result = tested.getPiecesOfColour(false);
        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void givenPiecesOfColour_whenGetPiecesOfColour_thenPieces() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        when(piece.isWhite()).thenReturn(false);
        tested = tested.withPieceAt(piece, Square.of(0, 0));
        // When
        var result = tested.getPiecesOfColour(false);
        // Then
        assertEquals(1, result.size());
        assertSame(piece, result.getFirst());
    }

    @Test
    public void givenNoPieces_whenGetPieces_thenEmptyList() {
        // Given
        // Board is already empty
        // When
        var result = tested.getPieces();
        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void givenPieces_whenGetPieces_thenPieces() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        when(piece.isWhite()).thenReturn(false);
        tested = tested.withPieceAt(piece, Square.of(0, 0));
        // When
        var result = tested.getPieces();
        // Then
        assertEquals(1, result.size());
        assertSame(piece, result.getFirst());
    }

    @Test
    public void givenNoPieces_whenGetPiecesOfType_AndColour_thenEmptyList() {
        // Given
        // Board is already empty
        // When
        var result = tested.getPiecesOfTypeAndColour(null, false);
        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void givenPiecesOfType_whenGetPiecesOfType_thenPiecesAndColour() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        when(piece.isWhite()).thenReturn(false);
        when(piece.pieceType()).thenReturn(StandardPieceType.KING);
        tested = tested.withPieceAt(piece, Square.of(0, 0));
        // When
        var result = tested.getPiecesOfTypeAndColour(StandardPieceType.KING, false);
        // Then
        assertEquals(1, result.size());
        assertSame(piece, result.getFirst());
    }

    @Test
    public void givenManyPieces_whenGetPiecesOfType_thenPiecesOfTypeAndColour() {
        // Given
        PieceType searchPieceType = mock(PieceType.class);
        PieceType otherPieceType = mock(PieceType.class);

        List<ChessPiece> searchedPieces = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ChessPiece piece = mock(ChessPiece.class);
            when(piece.isWhite()).thenReturn(false);
            when(piece.pieceType()).thenReturn(searchPieceType);
            searchedPieces.add(piece);
            tested = tested.withPieceAt(piece, Square.of(0, i));
        }

        for (int i = 0; i < 8; i++) {
            ChessPiece piece = mock(ChessPiece.class);
            when(piece.isWhite()).thenReturn(false);
            when(piece.pieceType()).thenReturn(otherPieceType);
            tested = tested.withPieceAt(piece, Square.of(1, i));
        }

        // When
        var result = tested.getPiecesOfTypeAndColour(searchPieceType, false);
        // Then
        assertEquals(searchedPieces.size(), result.size());
        assertIterableEquals(searchedPieces, result);
    }

    @Test
    public void givenNoPieces_whenWithPieceAt_thenPieceAtPosition() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        Square pieceSquare = Square.of(0, 0);
        // When
        tested = tested.withPieceAt(piece, pieceSquare);
        // Then
        assertSame(piece, tested.getPieceAt(pieceSquare));
        assertEquals(1, tested.getPieces().size());
    }

    @Test
    public void givenPieceAtPosition_whenWithoutPieceAt_thenNoPieceAtPosition() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        Square pieceSquare = Square.of(0, 0);
        tested = tested.withPieceAt(piece, pieceSquare);
        // When
        tested = tested.withoutPieceAt(pieceSquare);
        // Then
        assertNull(tested.getPieceAt(pieceSquare));
        assertEquals(0, tested.getPieces().size());
    }

    @Test
    public void givenPieceAtPosition_whenWithPieceMoved_thenPieceAtNewPosition() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        Square from = Square.of(0, 0);
        Square to = Square.of(1, 1);
        tested = tested.withPieceAt(piece, from);
        // When
        tested = tested.withPieceMoved(from, to);
        // Then
        assertNull(tested.getPieceAt(from));
        assertEquals(piece, tested.getPieceAt(to));
    }

    @Test
    public void givenPieces_whenWithAllPieces_thenAllPieces() {
        // Given
        ChessPiece piece1 = mock(ChessPiece.class);
        ChessPiece piece2 = mock(ChessPiece.class);
        Square square1 = Square.of(0, 0);
        Square square2 = Square.of(1, 1);

        Map<Square, ChessPiece> map = new HashMap<>();
        map.put(square1, piece1);
        map.put(square2, piece2);
        // When
        tested = tested.withAllPieces(map);
        // Then
        assertEquals(2, tested.getPieces().size());
        assertSame(piece1, tested.getPieceAt(square1));
        assertSame(piece2, tested.getPieceAt(square2));
    }

    @Test
    public void givenPieces_whenWithoutPieces_thenNoPieces() {
        // Given
        ChessPiece piece1 = mock(ChessPiece.class);
        ChessPiece piece2 = mock(ChessPiece.class);
        Square square1 = Square.of(0, 0);
        Square square2 = Square.of(1, 1);

        tested = tested.withPieceAt(piece1, square1);
        tested = tested.withPieceAt(piece2, square2);
        // When
        tested = tested.withoutPieces();
        // Then
        assertNull(tested.getPieceAt(square1));
        assertNull(tested.getPieceAt(square2));
        assertEquals(0, tested.getPieces().size());
    }

    @Test
    public void givenBounds_whenWithBounds_thenBounds() {
        // Given
        ChessBoardBounds newBounds = new ChessBoardBounds(4, 4);
        // When
        tested = tested.withBounds(newBounds);
        // Then
        assertEquals(newBounds, tested.getBounds());
    }
}
