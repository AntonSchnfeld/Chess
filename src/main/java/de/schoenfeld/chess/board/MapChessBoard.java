package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record MapChessBoard<T extends PieceType>(
        Map<Square, ChessPiece<T>> positionMap,
        ChessBoardBounds bounds
) implements ChessBoard<T> {

    public MapChessBoard {
        positionMap = Map.copyOf(positionMap);
    }

    public boolean isOccupied(Square square) {
        return getPieceAt(square) != null;
    }

    public MapChessBoard(ChessBoardBounds chessBoardBounds) {
        this(Map.of(), chessBoardBounds);
    }

    @Override
    public ChessPiece<T> getPieceAt(Square square) {
        return positionMap.get(square);
    }

    @Override
    public ChessBoardBounds getBounds() {
        return bounds;
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

    public MapChessBoard<T> withPieceAt(ChessPiece<T> piece, Square square) {
        if (!bounds.contains(square))
            throw new IllegalArgumentException("position must be in bounds");

        if (piece.equals(positionMap.get(square))) return this;

        Map<Square, ChessPiece<T>> newPositions = new HashMap<>(positionMap);
        newPositions.put(square, piece);
        return new MapChessBoard<>(newPositions, bounds);
    }

    public MapChessBoard<T> withoutPieceAt(Square square) {
        if (!bounds.contains(square))
            throw new IllegalArgumentException("position must be in bounds");
        if (!positionMap.containsKey(square)) return this;

        Map<Square, ChessPiece<T>> newPositions = new HashMap<>(positionMap);
        newPositions.remove(square);
        return new MapChessBoard<>(newPositions, bounds);
    }

    public MapChessBoard<T> withPieceMoved(Square from, Square to) {
        if (from.equals(to)) return this;
        ChessPiece<T> piece = getPieceAt(from);
        if (piece == null) return this;
        return withoutPieceAt(from).withPieceAt(piece, to);
    }

    public MapChessBoard<T> withAllPieces(Map<Square, ChessPiece<T>> newPieces) {
        if (newPieces.isEmpty()) return this;
        return new MapChessBoard<>(newPieces, bounds);
    }

    public MapChessBoard<T> withoutPieces() {
        return new MapChessBoard<>(Map.of(), bounds);
    }

    public MapChessBoard<T> withBounds(ChessBoardBounds newBounds) {
        if (newBounds.equals(bounds)) return this;
        return new MapChessBoard<>(positionMap, newBounds);
    }

    @Override
    public String toFen() {
        return "";
    }
}
