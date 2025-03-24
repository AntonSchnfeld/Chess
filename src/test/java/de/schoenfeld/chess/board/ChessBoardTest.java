package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public abstract class ChessBoardTest {

    protected ChessBoard<StandardPieceType> tested;
    protected ChessBoardBounds bounds;

    // Abstract method to initialize the tested object
    protected abstract void setUpBoard();

    @BeforeEach
    public void setUp() {
        bounds = new ChessBoardBounds(8, 8);
        setUpBoard();
    }

    @Test
    public void givenNoPieces_whenGetPieceAt_thenNull() {
        // Board is already empty
        ChessPiece<StandardPieceType> piece = tested.getPieceAt(Square.of(0, 0));
        assertNull(piece);
    }

    @Test
    public void givenPieceAtPosition_whenGetPieceAt_thenPiece() {
        ChessPiece<StandardPieceType> piece = mock(ChessPiece.class);
        Square pieceSquare = Square.of(0, 0);
        when(piece.isWhite()).thenReturn(false);

        tested = tested.withPieceAt(piece, pieceSquare);

        ChessPiece<StandardPieceType> result = tested.getPieceAt(pieceSquare);
        assertSame(piece, result);
    }

    @Test
    public void givenChessBoardBounds_whenGetBounds_thenBounds() {
        ChessBoardBounds result = tested.getBounds();
        assertEquals(bounds, result);
    }

    @Test
    public void givenNoPieces_whenGetPieces_thenEmptyList() {
        var result = tested.getOccupiedSquares();
        assertTrue(result.isEmpty());
    }

    @Test
    public void givenPieces_whenGetPieces_thenPieces() {
        ChessPiece<StandardPieceType> piece = mock(ChessPiece.class);
        when(piece.isWhite()).thenReturn(false);
        Square pieceSquare = Square.a1;
        tested = tested.withPieceAt(piece, pieceSquare);

        List<Square> result = tested.getOccupiedSquares();

        assertEquals(1, result.size());
        assertEquals(pieceSquare, result.getFirst());
    }

    @Test
    public void givenNoPieces_whenWithPieceAt_thenPieceAtPosition() {
        ChessPiece<StandardPieceType> piece = mock(ChessPiece.class);
        Square pieceSquare = Square.of(0, 0);

        tested = tested.withPieceAt(piece, pieceSquare);
        assertSame(piece, tested.getPieceAt(pieceSquare));
        assertEquals(1, tested.getOccupiedSquares().size());
    }

    @Test
    public void givenPieceAtPosition_whenWithoutPieceAt_thenNoPieceAtPosition() {
        ChessPiece<StandardPieceType> piece = mock(ChessPiece.class);
        Square pieceSquare = Square.of(0, 0);
        tested = tested.withPieceAt(piece, pieceSquare);

        tested = tested.withoutPieceAt(pieceSquare);
        assertNull(tested.getPieceAt(pieceSquare));
        assertEquals(0, tested.getOccupiedSquares().size());
    }

    @Test
    public void givenPieceAtPosition_whenWithPieceMoved_thenPieceAtNewPosition() {
        ChessPiece<StandardPieceType> piece = mock(ChessPiece.class);
        Square from = Square.of(0, 0);
        Square to = Square.of(1, 1);
        tested = tested.withPieceAt(piece, from);

        tested = tested.withPieceMoved(from, to);
        assertNull(tested.getPieceAt(from));
        assertEquals(piece, tested.getPieceAt(to));
    }

    @Test
    public void givenPieces_whenWithAllPieces_thenAllPieces() {
        ChessPiece<StandardPieceType> piece1 = mock(ChessPiece.class);
        ChessPiece<StandardPieceType> piece2 = mock(ChessPiece.class);
        Square square1 = Square.of(0, 0);
        Square square2 = Square.of(1, 1);

        Map<Square, ChessPiece<StandardPieceType>> map = new HashMap<>();
        map.put(square1, piece1);
        map.put(square2, piece2);

        tested = tested.withAllPieces(map);

        assertEquals(2, tested.getOccupiedSquares().size());
        assertSame(piece1, tested.getPieceAt(square1));
        assertSame(piece2, tested.getPieceAt(square2));
    }

    @Test
    public void givenPieces_whenWithoutPieces_thenNoPieces() {
        ChessPiece<StandardPieceType> piece1 = mock(ChessPiece.class);
        ChessPiece<StandardPieceType> piece2 = mock(ChessPiece.class);
        Square square1 = Square.of(0, 0);
        Square square2 = Square.of(1, 1);

        tested = tested.withPieceAt(piece1, square1);
        tested = tested.withPieceAt(piece2, square2);

        tested = tested.withoutPieces();
        assertNull(tested.getPieceAt(square1));
        assertNull(tested.getPieceAt(square2));
        assertEquals(0, tested.getOccupiedSquares().size());
    }

    @Test
    public void givenBounds_whenWithBounds_thenBounds() {
        ChessBoardBounds newBounds = new ChessBoardBounds(4, 4);
        tested = tested.withBounds(newBounds);
        assertEquals(newBounds, tested.getBounds());
    }
}
