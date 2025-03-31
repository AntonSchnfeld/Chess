package de.schoenfeld.chesskit.board;

import de.schoenfeld.chesskit.model.ChessBoardBounds;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.Square;

import java.io.Serial;
import java.util.*;
import java.util.stream.IntStream;

public class ListChessBoard<T extends PieceType> implements ChessBoard<T> {
    @Serial
    private static final long serialVersionUID = -6453375693718269360L;

    private List<ChessPiece<T>> pieces;
    private ChessBoardBounds bounds;

    public ListChessBoard(List<ChessPiece<T>> pieces, ChessBoardBounds bounds) {
        if (pieces == null)
            throw new NullPointerException("pieces");
        if (bounds == null)
            throw new NullPointerException("bounds");

        validateBoardSize(bounds, pieces);

        this.pieces = new ArrayList<>(pieces);
        this.bounds = bounds;
    }

    public ListChessBoard(ChessBoardBounds bounds) {
        this(ListChessBoard.createEmptyList(bounds), bounds);
    }

    private static <T extends PieceType> List<ChessPiece<T>> createEmptyList(ChessBoardBounds bounds) {
        return new ArrayList<>(Collections.nCopies(
                bounds.rows() * bounds.columns(),
                null
        ));
    }

    private static <T extends PieceType> void validateBoardSize(ChessBoardBounds bounds,
                                                                List<ChessPiece<T>> pieces) {
        int expectedSize = bounds.rows() * bounds.columns();
        if (pieces.size() != expectedSize) {
            throw new IllegalArgumentException(
                    "Invalid pieces list size. Expected: " + expectedSize +
                            ", Actual: " + pieces.size()
            );
        }
    }

    private static <T extends PieceType> String pieceToFenChar(ChessPiece<T> piece) {
        String base = piece.pieceType().symbol();
        return piece.isWhite() ? base.toUpperCase() : base;
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
    public ChessBoardBounds getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(ChessBoardBounds newBounds) {
        List<ChessPiece<T>> newPieces = new ArrayList<>(
                Collections.nCopies(newBounds.rows() * newBounds.columns(), null)
        );

        IntStream.range(0, Math.min(bounds.rows(), newBounds.rows()))
                .forEach(y -> IntStream.range(0, Math.min(bounds.columns(), newBounds.columns()))
                        .forEach(x -> {
                            Square pos = new Square(x, y);
                            if (newBounds.contains(pos)) {
                                newPieces.set(pos.x() + pos.y() * newBounds.columns(), getPieceAt(pos));
                            }
                        }));
        pieces = newPieces;
        this.bounds = newBounds;
    }

    @Override
    public List<Square> getSquaresWithColour(boolean isWhite) {
        List<Square> squares = new ArrayList<>();

        for (int i = 0; i < pieces.size(); i++) {
            ChessPiece<T> piece = pieces.get(i);
            if (piece != null && piece.isWhite() == isWhite) {
                squares.add(indexToSquare(i));
            }
        }

        return squares;
    }

    @Override
    public List<Square> getSquaresWithTypeAndColour(T pieceType, boolean isWhite) {
        List<Square> squares = new ArrayList<>();

        for (int i = 0; i < pieces.size(); i++) {
            ChessPiece<T> piece = pieces.get(i);
            if (piece != null && piece.isWhite() == isWhite && piece.pieceType().equals(pieceType)) {
                squares.add(indexToSquare(i));
            }
        }

        return squares;
    }

    @Override
    public List<Square> getSquaresWithType(T pieceType) {
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
    public List<Square> getOccupiedSquares() {
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
    public String toFen() {
        StringBuilder fen = new StringBuilder();

        for (int y = bounds.rows() - 1; y >= 0; y--) {
            int emptyCounter = 0;

            for (int x = 0; x < bounds.columns(); x++) {
                ChessPiece<T> piece = getPieceAt(Square.of(x, y));

                if (piece == null) {
                    emptyCounter++;
                } else {
                    if (emptyCounter > 0) {
                        fen.append(emptyCounter);
                        emptyCounter = 0;
                    }
                    fen.append(pieceToFenChar(piece));
                }
            }

            if (emptyCounter > 0) {
                fen.append(emptyCounter);
            }

            if (y > 0) {
                fen.append('/');
            }
        }

        return fen.toString();
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
                toFen() +
                '}';
    }
}