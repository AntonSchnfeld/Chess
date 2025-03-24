package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;

import java.util.*;
import java.util.stream.IntStream;

public record ListChessBoard<T extends PieceType>(
        List<ChessPiece<T>> pieces,
        ChessBoardBounds bounds
) implements ChessBoard<T> {

    public ListChessBoard {
        Objects.requireNonNull(pieces, "Pieces list cannot be null");
        Objects.requireNonNull(bounds, "Bounds cannot be null");
        ArrayList<ChessPiece<T>> wrapper = new ArrayList<>(pieces);
        wrapper.ensureCapacity(bounds.rows() * bounds.columns());
        pieces = Collections.unmodifiableList(wrapper);
        validateBoardSize(bounds, pieces);
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

    public boolean isOccupied(Square square) {
        return getPieceAt(square) != null;
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

    private int calculateIndex(Square square) {
        if (!bounds.contains(square)) {
            throw new IndexOutOfBoundsException("Position out of bounds: " + square);
        }
        return square.x() + square.y() * bounds.columns();
    }

    private static <T extends PieceType> String pieceToFenChar(ChessPiece<T> piece) {
        String base = piece.pieceType().symbol();
        return piece.isWhite() ? base.toUpperCase() : base;
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
    public ListChessBoard<T> withPieceAt(ChessPiece<T> piece, Square square) {
        List<ChessPiece<T>> newPieces = new ArrayList<>(pieces);
        newPieces.set(calculateIndex(square), piece);
        return new ListChessBoard<>(newPieces, bounds);
    }

    @Override
    public ListChessBoard<T> withoutPieceAt(Square square) {
        List<ChessPiece<T>> newPieces = new ArrayList<>(pieces);
        newPieces.set(calculateIndex(square), null);
        return new ListChessBoard<>(newPieces, bounds);
    }

    @Override
    public ListChessBoard<T> withPieceMoved(Square from, Square to) {
        ChessPiece<T> piece = getPieceAt(from);
        List<ChessPiece<T>> newPieces = new ArrayList<>(pieces);
        newPieces.set(calculateIndex(from), null);
        newPieces.set(calculateIndex(to), piece);
        return new ListChessBoard<>(newPieces, bounds);
    }

    @Override
    public ListChessBoard<T> withAllPieces(Map<Square, ChessPiece<T>> pieces) {
        List<ChessPiece<T>> newPieces = createEmptyList(bounds);
        pieces.forEach((pos, piece) ->
                newPieces.set(calculateIndex(pos), piece)
        );
        return new ListChessBoard<>(newPieces, bounds);
    }

    @Override
    public ListChessBoard<T> withoutPieces() {
        return new ListChessBoard<>(createEmptyList(bounds), bounds);
    }

    @Override
    public ListChessBoard<T> withBounds(ChessBoardBounds newBounds) {
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

        return new ListChessBoard<>(newPieces, newBounds);
    }

    @Override
    public String toFen() {
        StringBuilder fen = new StringBuilder();
        for (int y = bounds.rows() - 1; y >= 0; y--) {
            int emptyCounter = 0;

            for (int x = 0; x < bounds.columns(); x++) {
                ChessPiece<T> piece = getPieceAt(new Square(x, y));

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

            if (emptyCounter > 0) fen.append(emptyCounter);
            if (y > 0) fen.append('/');
        }
        return fen.toString();
    }
}