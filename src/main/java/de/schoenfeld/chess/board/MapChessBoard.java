package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;

import java.io.Serial;
import java.util.*;

public class MapChessBoard<T extends PieceType> implements ChessBoard<T> {
    @Serial
    private static final long serialVersionUID = -3408361097786300767L;

    private Map<Square, ChessPiece<T>> positionMap;
    private ChessBoardBounds bounds;

    public MapChessBoard(Map<Square, ChessPiece<T>> positionMap, ChessBoardBounds bounds) {
        this.positionMap = new HashMap<>(positionMap);
        this.bounds = bounds;
    }

    public MapChessBoard(ChessBoardBounds bounds) {
        this(Map.of(), bounds);
    }

    public MapChessBoard() {
        this(new ChessBoardBounds(8, 8));
    }

    public boolean isOccupied(Square square) {
        return getPieceAt(square) != null;
    }

    @Override
    public ChessPiece<T> getPieceAt(Square square) {
        return positionMap.get(square);
    }

    @Override
    public ChessBoardBounds getBounds() {
        return bounds;
    }

    public void setBounds(ChessBoardBounds newBounds) {
        if (newBounds.equals(bounds)) return;
        bounds = newBounds;
    }

    @Override
    public List<Square> getSquaresWithColour(boolean isWhite) {
        List<Square> pieces = new ArrayList<>(positionMap.size() / 2);

        for (Map.Entry<Square, ChessPiece<T>> entry : positionMap.entrySet())
            if (entry.getValue().isWhite() == isWhite)
                pieces.add(entry.getKey());

        return pieces;
    }

    @Override
    public List<Square> getOccupiedSquares() {
        List<Square> squares = new ArrayList<>(positionMap.size());

        for (Map.Entry<Square, ChessPiece<T>> entry : positionMap.entrySet())
            squares.add(entry.getKey());

        return squares;
    }

    @Override
    public List<Square> getSquaresWithTypeAndColour(T pieceType, boolean isWhite) {
        List<Square> pieces = new ArrayList<>((positionMap.size() / 2) / 8);

        for (Map.Entry<Square, ChessPiece<T>> entry : positionMap.entrySet())
            if (entry.getValue().isWhite() == isWhite &&
                    entry.getValue().pieceType().equals(pieceType))
                pieces.add(entry.getKey());

        return pieces;
    }

    @Override
    public List<Square> getSquaresWithType(T pieceType) {
        List<Square> pieces = new ArrayList<>(positionMap.size() / 8);

        for (Map.Entry<Square, ChessPiece<T>> entry : positionMap.entrySet())
            if (entry.getValue().pieceType().equals(pieceType))
                pieces.add(entry.getKey());

        return pieces;
    }

    public void setPieceAt(Square square, ChessPiece<T> piece) {
        if (!bounds.contains(square))
            throw new IllegalArgumentException("position must be in bounds");

        if (piece.equals(positionMap.get(square))) return;

        positionMap.put(square, piece);
    }

    public void removePieceAt(Square square) {
        if (!bounds.contains(square))
            throw new IllegalArgumentException("position must be in bounds");
        if (!positionMap.containsKey(square)) return;

        positionMap.remove(square);
    }

    public void movePiece(Square from, Square to) {
        if (!bounds.contains(from))
            throw new IllegalArgumentException("from must be in bounds");
        if (!bounds.contains(to))
            throw new IllegalArgumentException("to must be in bounds");

        if (from.equals(to)) return;

        ChessPiece<T> piece = positionMap.get(from);
        if (piece == null) return;

        positionMap.remove(from);
        positionMap.put(to, piece);
    }

    public void setAllPieces(Map<Square, ChessPiece<T>> newPieces) {
        positionMap = new HashMap<>(newPieces);
    }

    public void removePieces() {
        positionMap.clear();
    }

    @Override
    public String toFen() {
        StringBuilder fen = new StringBuilder();

        // For each rank (8 to 1 in standard chess boards)
        for (int rank = bounds.rows() - 1; rank >= 0; rank--) {
            int emptyCount = 0;

            // Iterate over each file (0 to 7 in standard chess boards)
            for (int file = 0; file < bounds.columns(); file++) {
                Square square = Square.of(file, rank);
                ChessPiece<T> piece = positionMap.get(square);

                if (piece == null) {
                    // Increment empty square counter if the square is empty
                    emptyCount++;
                } else {
                    // If we were counting empty squares, add that number to the FEN string
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }

                    // Append the piece to the FEN string (capitalize for white, lowercase for black)
                    fen.append(piece.isWhite() ? piece.pieceType().symbol() : piece.pieceType().symbol().toLowerCase());
                }
            }

            // Append the empty square count (if any) at the end of the rank
            if (emptyCount > 0) {
                fen.append(emptyCount);
            }

            // If it's not the last rank, add a separator
            if (rank > 0) {
                fen.append('/');
            }
        }

        // For now, we'll just return the piece placement. You can add the rest of the FEN string later.
        return fen.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        MapChessBoard<?> that = (MapChessBoard<?>) object;
        return Objects.equals(positionMap, that.positionMap) && Objects.equals(bounds, that.bounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionMap, bounds);
    }

    @Override
    public String toString() {
        return "MapChessBoard{" +
                toFen() +
                '}';
    }
}
