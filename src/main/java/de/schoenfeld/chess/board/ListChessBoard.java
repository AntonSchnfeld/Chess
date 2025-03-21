package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;

import java.util.*;
import java.util.stream.IntStream;

public record ListChessBoard<T extends PieceType>(
        List<ChessPiece> pieces,
        ChessBoardBounds bounds
) implements ChessBoard<T> {

    public ListChessBoard {
        Objects.requireNonNull(pieces, "Pieces list cannot be null");
        Objects.requireNonNull(bounds, "Bounds cannot be null");
        ArrayList<ChessPiece> wrapper = new ArrayList<>(pieces);
        wrapper.ensureCapacity(bounds.rows() * bounds.columns());
        pieces = Collections.unmodifiableList(wrapper);
        validateBoardSize(bounds, pieces);
    }

    public ListChessBoard(ChessBoardBounds bounds) {
        this(createEmptyList(bounds), bounds);
    }

    private static List<ChessPiece> createEmptyList(ChessBoardBounds bounds) {
        return new ArrayList<>(Collections.nCopies(
                bounds.rows() * bounds.columns(),
                null
        ));
    }

    private void validateBoardSize(ChessBoardBounds bounds, List<ChessPiece> pieces) {
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

    @Override
    public ChessPiece getPieceAt(Square square) {
        return pieces.get(calculateIndex(square));
    }

    @Override
    public Square getPiecePosition(ChessPiece chessPiece) {
        int index = IntStream.range(0, pieces.size())
                .filter(i -> chessPiece.equals(pieces.get(i)))
                .findFirst()
                .orElse(-1);

        if (index == -1) return null;

        int y = index / bounds.columns();
        int x = index % bounds.columns();
        return new Square(x, y);
    }

    @Override
    public ChessBoardBounds getBounds() {
        return bounds;
    }

    @Override
    public List<ChessPiece> getPiecesOfColour(boolean isWhite) {
        return pieces.stream()
                .filter(Objects::nonNull)
                .filter(p -> p.isWhite() == isWhite)
                .toList();
    }

    @Override
    public List<ChessPiece> getPiecesOfTypeAndColour(T pieceType, boolean colour) {
        return pieces.stream()
                .filter(Objects::nonNull)
                .filter(p -> p.pieceType().equals(pieceType) && p.isWhite() == colour)
                .toList();
    }

    @Override
    public List<ChessPiece> getPiecesOfType(T pieceType) {
        return pieces.stream()
                .filter(p -> p != null && p.pieceType().equals(pieceType))
                .toList();
    }

    @Override
    public ListChessBoard<T> withPieceAt(ChessPiece piece, Square square) {
        List<ChessPiece> newPieces = new ArrayList<>(pieces);
        newPieces.set(calculateIndex(square), piece);
        return new ListChessBoard<>(newPieces, bounds);
    }

    @Override
    public ListChessBoard<T> withoutPieceAt(Square square) {
        List<ChessPiece> newPieces = new ArrayList<>(pieces);
        newPieces.set(calculateIndex(square), null);
        return new ListChessBoard<>(newPieces, bounds);
    }

    @Override
    public ListChessBoard<T> withPieceMoved(Square from, Square to) {
        ChessPiece piece = getPieceAt(from);
        List<ChessPiece> newPieces = new ArrayList<>(pieces);
        newPieces.set(calculateIndex(from), null);
        newPieces.set(calculateIndex(to), piece.withMoved(true));
        return new ListChessBoard<>(newPieces, bounds);
    }

    @Override
    public ListChessBoard<T> withAllPieces(Map<Square, ChessPiece> pieces) {
        List<ChessPiece> newPieces = createEmptyList(bounds);
        pieces.forEach((pos, piece) ->
                newPieces.set(calculateIndex(pos), piece)
        );
        return new ListChessBoard<>(newPieces, bounds);
    }

    @Override
    public List<ChessPiece> getPieces() {
        return pieces.stream()
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public ListChessBoard<T> withoutPieces() {
        return new ListChessBoard<>(createEmptyList(bounds), bounds);
    }

    @Override
    public ListChessBoard<T> withBounds(ChessBoardBounds newBounds) {
        List<ChessPiece> newPieces = new ArrayList<>(
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
                ChessPiece piece = getPieceAt(new Square(x, y));

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

    private String pieceToFenChar(ChessPiece piece) {
        String base = piece.pieceType().symbol();
        return piece.isWhite() ? base.toUpperCase() : base;
    }
}