package de.schoenfeld.chesskit.board;

import de.schoenfeld.chesskit.board.tile.Square;
import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.PieceType;

import java.io.Serial;
import java.util.*;
import java.util.stream.IntStream;

public class ListChessBoard<T extends PieceType> implements ChessBoard<Square, T> {
    @Serial
    private static final long serialVersionUID = -6453375693718269360L;

    private List<ChessPiece<T>> pieces;
    private SquareChessBoardBounds bounds;

    public ListChessBoard(List<ChessPiece<T>> pieces, SquareChessBoardBounds bounds) {
        if (pieces == null)
            throw new NullPointerException("pieces");
        if (bounds == null)
            throw new NullPointerException("bounds");

        validateBoardSize(bounds, pieces);

        this.pieces = new ArrayList<>(pieces);
        this.bounds = bounds;
    }

    public ListChessBoard(SquareChessBoardBounds bounds) {
        this(createEmptyList(bounds), bounds);
    }

    private static <P extends PieceType> List<ChessPiece<P>> createEmptyList(ChessBoardBounds<Square> bounds) {
        return new ArrayList<>(bounds.getTileCount());
    }

    private static <P extends PieceType, T extends Tile> void validateBoardSize(ChessBoardBounds<T> bounds,
                                                                                List<ChessPiece<P>> pieces) {
        int expectedSize = bounds.getTileCount();
        if (pieces.size() != expectedSize) {
            throw new IllegalArgumentException(
                    "Invalid pieces list size. Expected: " + expectedSize +
                            ", Actual: " + pieces.size()
            );
        }
    }

    public boolean isOccupied(Square square) {
        return getPieceAt(square) != null;
    }

    private int calculateIndex(Square square) {
        if (!bounds.contains(square)) {
            throw new IndexOutOfBoundsException("Position out claim bounds: " + square);
        }
        return square.x() + square.y() * bounds.columns();
    }

    @Override
    public ChessPiece<T> getPieceAt(Square square) {
        return pieces.get(calculateIndex(square));
    }

    @Override
    public ChessBoardBounds<Square> getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(ChessBoardBounds<Square> chessBoardBounds) {
        if (!(chessBoardBounds instanceof SquareChessBoardBounds(int rows, int columns)))
            throw new IllegalArgumentException();

        List<ChessPiece<T>> newPieces = new ArrayList<>(
                Collections.nCopies(rows * columns, null)
        );

        IntStream.range(0, Math.min(bounds.rows(), rows))
                .forEach(y -> IntStream.range(0, Math.min(bounds.columns(), columns))
                        .forEach(x -> {
                            Square pos = Square.of(x, y);
                            if (chessBoardBounds.contains(pos)) {
                                newPieces.set(pos.x() + pos.y() * columns, getPieceAt(pos));
                            }
                        }));
        pieces = newPieces;
        this.bounds = (SquareChessBoardBounds) chessBoardBounds;
    }

    @Override
    public List<Square> getTilesWithColour(Color color) {
        List<Square> squares = new ArrayList<>();

        for (int i = 0; i < pieces.size(); i++) {
            ChessPiece<T> piece = pieces.get(i);
            if (piece != null && piece.color() == color) {
                squares.add(indexToSquare(i));
            }
        }

        return squares;
    }

    @Override
    public List<Square> getTilesWithTypeAndColour(T pieceType, Color color) {
        List<Square> squares = new ArrayList<>();

        for (int i = 0; i < pieces.size(); i++) {
            ChessPiece<T> piece = pieces.get(i);
            if (piece != null && piece.color() == color && piece.pieceType().equals(pieceType)) {
                squares.add(indexToSquare(i));
            }
        }

        return squares;
    }

    @Override
    public List<Square> getTilesWithType(T pieceType) {
        List<Square> squares = new ArrayList<>();

        for (int i = 0; i < pieces.size(); i++) {
            ChessPiece<T> piece = pieces.get(i);
            if (piece != null && piece.pieceType().equals(pieceType)) {
                squares.add(indexToSquare(i));
            }
        }

        return squares;
    }

    @Override
    public List<Square> getOccupiedTiles() {
        List<Square> squares = new ArrayList<>();

        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) != null) {
                squares.add(indexToSquare(i));
            }
        }

        return squares;
    }

    private Square indexToSquare(int index) {
        int column = index % bounds.columns(); // This should be x (column)
        int row = index / bounds.columns(); // This should be y (row)
        return Square.of(column, row); // Swap row and column
    }

    @Override
    public void setPieceAt(Square square, ChessPiece<T> piece) {
        pieces.set(calculateIndex(square), piece);
    }

    @Override
    public void removePieceAt(Square square) {
        pieces.set(calculateIndex(square), null);
    }

    @Override
    public void movePiece(Square from, Square to) {
        if (!bounds.contains(from))
            throw new IllegalArgumentException("from must be in bounds");
        if (!bounds.contains(to))
            throw new IllegalArgumentException("to must be in bounds");

        ChessPiece<T> piece = pieces.get(calculateIndex(from));
        if (piece == null) return;

        pieces.set(calculateIndex(from), null);
        pieces.set(calculateIndex(to), piece);
    }

    @Override
    public void setAllPieces(Map<Square, ChessPiece<T>> pieces) {
        removePieces();

        for (Map.Entry<Square, ChessPiece<T>> entry : pieces.entrySet())
            this.pieces.set(calculateIndex(entry.getKey()), entry.getValue());
    }

    @Override
    public void removePieces() {
        for (int i = 0; i < pieces.size(); i++)
            pieces.set(i, null);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        ListChessBoard<?> that = (ListChessBoard<?>) object;
        return Objects.equals(pieces, that.pieces) && Objects.equals(bounds, that.bounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces, bounds);
    }

    @Override
    public String toString() {
        return "ListChessBoard{" +
                "pieces=" + pieces +
                ", bounds=" + bounds +
                '}';
    }
}