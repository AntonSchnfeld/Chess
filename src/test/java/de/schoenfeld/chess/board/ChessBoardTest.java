package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public abstract class ChessBoardTest {

    protected ChessBoard<StandardPieceType> tested;
    protected ChessBoardBounds bounds;

    // Abstract method to initialize the concrete ChessBoard implementation
    protected abstract void setUpBoard();

    @BeforeEach
    public void setUp() {
        bounds = new ChessBoardBounds(8, 8);
        setUpBoard();
        tested.setBounds(bounds);
    }

    @Test
    public void givenEmptyBoard_whenGetPieceAt_thenNull() {
        assertNull(tested.getPieceAt(Square.of(0, 0)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenPieceOnBoard_whenGetPieceAt_thenReturnsPiece() {
        ChessPiece<StandardPieceType> piece = mock(ChessPiece.class);
        Square pieceSquare = Square.of(0, 0);
        tested.setPieceAt(pieceSquare, piece);

        assertSame(piece, tested.getPieceAt(pieceSquare));
    }

    @Test
    public void givenBoard_whenGetBounds_thenReturnsBounds() {
        assertEquals(bounds, tested.getBounds());
    }

    @Test
    public void givenEmptyBoard_whenGetOccupiedSquares_thenEmptyList() {
        assertTrue(tested.getOccupiedSquares().isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenBoardWithPiece_whenGetOccupiedSquares_thenReturnsSquares() {
        ChessPiece<StandardPieceType> piece = mock(ChessPiece.class);
        Square pieceSquare = Square.of(0, 0);
        tested.setPieceAt(pieceSquare, piece);

        List<Square> occupiedSquares = tested.getOccupiedSquares();
        assertEquals(1, occupiedSquares.size());
        assertEquals(pieceSquare, occupiedSquares.getFirst());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenPieceOnBoard_whenRemovePieceAt_thenSquareIsEmpty() {
        ChessPiece<StandardPieceType> piece = mock(ChessPiece.class);
        Square pieceSquare = Square.of(0, 0);
        tested.setPieceAt(pieceSquare, piece);

        tested.removePieceAt(pieceSquare);

        assertNull(tested.getPieceAt(pieceSquare));
        assertTrue(tested.getOccupiedSquares().isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenPieceOnBoard_whenMovePiece_thenPieceMovesCorrectly() {
        ChessPiece<StandardPieceType> piece = mock(ChessPiece.class);
        Square from = Square.of(0, 0);
        Square to = Square.of(1, 1);
        tested.setPieceAt(from, piece);

        tested.movePiece(from, to);

        assertNull(tested.getPieceAt(from));
        assertSame(piece, tested.getPieceAt(to));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenMultiplePieces_whenSetAllPieces_thenBoardContainsThem() {
        ChessPiece<StandardPieceType> piece1 = mock(ChessPiece.class);
        ChessPiece<StandardPieceType> piece2 = mock(ChessPiece.class);
        Square square1 = Square.of(0, 0);
        Square square2 = Square.of(1, 1);

        Map<Square, ChessPiece<StandardPieceType>> pieces = new HashMap<>();
        pieces.put(square1, piece1);
        pieces.put(square2, piece2);

        tested.setAllPieces(pieces);

        assertEquals(2, tested.getOccupiedSquares().size());
        assertSame(piece1, tested.getPieceAt(square1));
        assertSame(piece2, tested.getPieceAt(square2));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenBoardWithPieces_whenRemovePieces_thenBoardIsEmpty() {
        ChessPiece<StandardPieceType> piece1 = mock(ChessPiece.class);
        ChessPiece<StandardPieceType> piece2 = mock(ChessPiece.class);
        Square square1 = Square.of(0, 0);
        Square square2 = Square.of(1, 1);

        tested.setPieceAt(square1, piece1);
        tested.setPieceAt(square2, piece2);

        tested.removePieces();

        assertTrue(tested.getOccupiedSquares().isEmpty());
        assertNull(tested.getPieceAt(square1));
        assertNull(tested.getPieceAt(square2));
    }

    @Test
    public void givenNewBounds_whenSetBounds_thenBoardHasNewBounds() {
        ChessBoardBounds newBounds = new ChessBoardBounds(4, 4);
        tested.setBounds(newBounds);

        assertEquals(newBounds, tested.getBounds());
    }
}
