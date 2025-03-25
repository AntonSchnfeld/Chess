package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void setPieceAt(ChessPiece<T> piece, Square square) {
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

    public void setBounds(ChessBoardBounds newBounds) {
        if (newBounds.equals(bounds)) return;
        bounds = newBounds;
    }

    @Override
    public String toFen() {
        return "";
    }
}
