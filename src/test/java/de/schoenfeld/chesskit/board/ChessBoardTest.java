package de.schoenfeld.chesskit.board;

import de.schoenfeld.chesskit.board.tile.Square;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.StandardPieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public abstract class ChessBoardTest {

    protected ChessBoard<Square, StandardPieceType> tested;
    protected SquareChessBoardBounds bounds;

    // Abstract method to initialize the concrete ChessBoard implementation
    protected abstract void setUpBoard();

    @BeforeEach
    public void setUp() {
        bounds = new SquareChessBoardBounds(8, 8);
        setUpBoard();
        tested.setBounds(bounds);
    }

    @Test
    public void givenEmptyBoard_whenGetPieceAt_thenNull() {
        assertNull(tested.getPieceAt(Square8x8.of(0, 0)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenPieceOnBoard_whenGetPieceAt_thenReturnsPiece() {
        ChessPiece<StandardPieceType> piece = mock(ChessPiece.class);
        Square8x8 pieceSquare8x8 = Square8x8.of(0, 0);
        tested.setPieceAt(pieceSquare8x8, piece);

        assertSame(piece, tested.getPieceAt(pieceSquare8x8));
    }

    @Test
    public void givenBoard_whenGetBounds_thenReturnsBounds() {
        assertEquals(bounds, tested.getBounds());
    }

    @Test
    public void givenEmptyBoard_whenGetOccupiedTiles_thenEmptyList() {
        assertTrue(tested.getOccupiedTiles().isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenBoardWithPiece_whenGetOccupiedSquares_thenReturnsTiles() {
        ChessPiece<StandardPieceType> piece = mock(ChessPiece.class);
        Square8x8 pieceSquare8x8 = Square8x8.of(0, 0);
        tested.setPieceAt(pieceSquare8x8, piece);

        List<Square> occupiedSquare8x8s = tested.getOccupiedTiles();
        assertEquals(1, occupiedSquare8x8s.size());
        assertEquals(pieceSquare8x8, occupiedSquare8x8s.getFirst());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenPieceOnBoard_whenRemovePieceAt_thenSquareIsEmpty() {
        ChessPiece<StandardPieceType> piece = mock(ChessPiece.class);
        Square8x8 pieceSquare8x8 = Square8x8.of(0, 0);
        tested.setPieceAt(pieceSquare8x8, piece);

        tested.removePieceAt(pieceSquare8x8);

        assertNull(tested.getPieceAt(pieceSquare8x8));
        assertTrue(tested.getOccupiedTiles().isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenPieceOnBoard_whenMovePiece_thenPieceMovesCorrectly() {
        ChessPiece<StandardPieceType> piece = mock(ChessPiece.class);
        Square8x8 from = Square8x8.of(0, 0);
        Square8x8 to = Square8x8.of(1, 1);
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
        Square8x8 square8x81 = Square8x8.of(0, 0);
        Square8x8 square8x82 = Square8x8.of(1, 1);

        Map<Square, ChessPiece<StandardPieceType>> pieces = new HashMap<>();
        pieces.put(square8x81, piece1);
        pieces.put(square8x82, piece2);

        tested.setAllPieces(pieces);

        assertEquals(2, tested.getOccupiedTiles().size());
        assertSame(piece1, tested.getPieceAt(square8x81));
        assertSame(piece2, tested.getPieceAt(square8x82));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenBoardWithPieces_whenRemovePieces_thenBoardIsEmpty() {
        ChessPiece<StandardPieceType> piece1 = mock(ChessPiece.class);
        ChessPiece<StandardPieceType> piece2 = mock(ChessPiece.class);
        Square8x8 square8x81 = Square8x8.of(0, 0);
        Square8x8 square8x82 = Square8x8.of(1, 1);

        tested.setPieceAt(square8x81, piece1);
        tested.setPieceAt(square8x82, piece2);

        tested.removePieces();

        assertTrue(tested.getOccupiedTiles().isEmpty());
        assertNull(tested.getPieceAt(square8x81));
        assertNull(tested.getPieceAt(square8x82));
    }

    @Test
    public void givenNewBounds_whenSetBounds_thenBoardHasNewBounds() {
        ChessBoardBounds<Square> newBounds = new SquareChessBoardBounds(8, 8);
        tested.setBounds(newBounds);

        assertEquals(newBounds, tested.getBounds());
    }
}
