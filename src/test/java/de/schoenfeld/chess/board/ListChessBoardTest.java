package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;
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
    private ListChessBoard tested;
    private ChessBoardBounds bounds;

    @BeforeEach
    public void setUp() {
        bounds = new ChessBoardBounds(8, 8);
        tested = new ListChessBoard(bounds);
    }

    @Test
    public void givenNoPieces_whenGetPieceAt_thenNull() {
        // Given
        // Board is already empty
        // When
        ChessPiece piece = tested.getPieceAt(new Position(0, 0));
        // Then
        assertNull(piece);
    }

    @Test
    public void givenPieceAtPosition_whenGetPieceAt_thenPiece() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        Position piecePosition = Position.of(0, 0);

        when(piece.isWhite()).thenReturn(false);

        tested = tested.withPieceAt(piece, piecePosition);
        // When
        ChessPiece result = tested.getPieceAt(piecePosition);
        // Then
        assertSame(piece, result);
    }

    @Test
    public void givenPieceAtPosition_whenGetPiecePosition_thenPosition() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        Position piecePosition = Position.of(0, 0);

        when(piece.isWhite()).thenReturn(false);

        tested = tested.withPieceAt(piece, piecePosition);
        // When
        Position result = tested.getPiecePosition(piece);
        // Then
        assertEquals(piecePosition, result);
    }

    @Test
    public void givenNoPieces_whenGetPiecePosition_thenNull() {
        // Given
        // Board is already empty
        // When
        Position result = tested.getPiecePosition(mock(ChessPiece.class));
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
        tested = tested.withPieceAt(piece, Position.of(0, 0));
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
        tested = tested.withPieceAt(piece, Position.of(0, 0));
        // When
        var result = tested.getPieces();
        // Then
        assertEquals(1, result.size());
        assertSame(piece, result.getFirst());
    }

    @Test
    public void givenNoPieces_whenGetPiecesOfType_thenEmptyList() {
        // Given
        // Board is already empty
        // When
        var result = tested.getPiecesOfType(null, false);
        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void givenPiecesOfType_whenGetPiecesOfType_thenPieces() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        when(piece.isWhite()).thenReturn(false);
        tested = tested.withPieceAt(piece, Position.of(0, 0));
        // When
        var result = tested.getPiecesOfType(null, false);
        // Then
        assertEquals(1, result.size());
        assertSame(piece, result.getFirst());
    }

    @Test
    public void givenManyPieces_whenGetPiecesOfType_thenPiecesOfType() {
        // Given
        PieceType searchPieceType = mock(PieceType.class);
        PieceType otherPieceType = mock(PieceType.class);

        List<ChessPiece> searchedPieces = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ChessPiece piece = mock(ChessPiece.class);
            when(piece.isWhite()).thenReturn(false);
            when(piece.pieceType()).thenReturn(searchPieceType);
            searchedPieces.add(piece);
            tested = tested.withPieceAt(piece, Position.of(0, i));
        }

        for (int i = 0; i < 8; i++) {
            ChessPiece piece = mock(ChessPiece.class);
            when(piece.isWhite()).thenReturn(false);
            when(piece.pieceType()).thenReturn(otherPieceType);
            tested = tested.withPieceAt(piece, Position.of(1, i));
        }

        // When
        var result = tested.getPiecesOfType(searchPieceType, false);
        // Then
        assertEquals(searchedPieces.size(), result.size());
        assertIterableEquals(searchedPieces, result);
    }

    @Test
    public void givenNoPieces_whenWithPieceAt_thenPieceAtPosition() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        Position piecePosition = Position.of(0, 0);
        // When
        tested = tested.withPieceAt(piece, piecePosition);
        // Then
        assertSame(piece, tested.getPieceAt(piecePosition));
        assertEquals(1, tested.getPieces().size());
    }

    @Test
    public void givenPieceAtPosition_whenWithoutPieceAt_thenNoPieceAtPosition() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        Position piecePosition = Position.of(0, 0);
        tested = tested.withPieceAt(piece, piecePosition);
        // When
        tested = tested.withoutPieceAt(piecePosition);
        // Then
        assertNull(tested.getPieceAt(piecePosition));
        assertEquals(0, tested.getPieces().size());
    }

    @Test
    public void givenPieceAtPosition_whenWithPieceMoved_thenPieceAtNewPosition() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        Position from = Position.of(0, 0);
        Position to = Position.of(1, 1);
        tested = tested.withPieceAt(piece, from);
        // When
        tested = tested.withPieceMoved(from, to);
        // Then
        assertNull(tested.getPieceAt(from));
        assertSame(piece, tested.getPieceAt(to));
    }

    @Test
    public void givenPieces_whenWithAllPieces_thenAllPieces() {
        // Given
        ChessPiece piece1 = mock(ChessPiece.class);
        ChessPiece piece2 = mock(ChessPiece.class);
        Position position1 = Position.of(0, 0);
        Position position2 = Position.of(1, 1);

        Map<Position, ChessPiece> map = new HashMap<>();
        map.put(position1, piece1);
        map.put(position2, piece2);
        // When
        tested = tested.withAllPieces(map);
        // Then
        assertEquals(2, tested.getPieces().size());
        assertSame(piece1, tested.getPieceAt(position1));
        assertSame(piece2, tested.getPieceAt(position2));
    }

    @Test
    public void givenPieces_whenWithoutPieces_thenNoPieces() {
        // Given
        ChessPiece piece1 = mock(ChessPiece.class);
        ChessPiece piece2 = mock(ChessPiece.class);
        Position position1 = Position.of(0, 0);
        Position position2 = Position.of(1, 1);

        tested = tested.withPieceAt(piece1, position1);
        tested = tested.withPieceAt(piece2, position2);
        // When
        tested = tested.withoutPieces();
        // Then
        assertNull(tested.getPieceAt(position1));
        assertNull(tested.getPieceAt(position2));
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
