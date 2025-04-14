package de.schoenfeld.chesskit.board;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.PieceType;

import java.io.Serial;
import java.util.*;

public class MapChessBoard<T extends Tile, P extends PieceType> implements ChessBoard<T, P> {
    @Serial
    private static final long serialVersionUID = -3408361097786300767L;

    private Map<T, ChessPiece<P>> positionMap;
    private ChessBoardBounds<T> bounds;

    public MapChessBoard(Map<T, ChessPiece<P>> positionMap, ChessBoardBounds<T> bounds) {
        this.positionMap = new HashMap<>(positionMap);
        this.bounds = bounds;
    }

    public MapChessBoard(ChessBoardBounds<T> bounds) {
        this(Map.of(), bounds);
    }

    @Override
    public boolean isOccupied(T square8x8) {
        return getPieceAt(square8x8) != null;
    }

    @Override
    public ChessPiece<P> getPieceAt(T square8x8) {
        return positionMap.get(square8x8);
    }

    @Override
    public ChessBoardBounds<T> getBounds() {
        return bounds;
    }

    public void setBounds(ChessBoardBounds<T> newBounds) {
        if (newBounds.equals(bounds)) return;
        bounds = newBounds;
    }

    @Override
    public List<T> getTilesWithColour(Color color) {
        List<T> pieces = new ArrayList<>(positionMap.size() / 2);

        for (Map.Entry<T, ChessPiece<P>> entry : positionMap.entrySet())
            if (entry.getValue().color() == color)
                pieces.add(entry.getKey());

        return pieces;
    }

    @Override
    public List<T> getOccupiedTiles() {
        List<T> square8x8s = new ArrayList<>(positionMap.size());

        for (Map.Entry<T, ChessPiece<P>> entry : positionMap.entrySet())
            square8x8s.add(entry.getKey());

        return square8x8s;
    }

    @Override
    public List<T> getTilesWithTypeAndColour(P pieceType, Color color) {
        List<T> pieces = new ArrayList<>((positionMap.size() / 2) / 8);

        for (Map.Entry<T, ChessPiece<P>> entry : positionMap.entrySet())
            if (entry.getValue().color() == color &&
                    entry.getValue().pieceType().equals(pieceType))
                pieces.add(entry.getKey());

        return pieces;
    }

    @Override
    public List<T> getTilesWithType(P pieceType) {
        List<T> pieces = new ArrayList<>(positionMap.size() / 8);

        for (Map.Entry<T, ChessPiece<P>> entry : positionMap.entrySet())
            if (entry.getValue().pieceType().equals(pieceType))
                pieces.add(entry.getKey());

        return pieces;
    }

    public void setPieceAt(T tile, ChessPiece<P> piece) {
        if (!bounds.contains(tile))
            throw new IllegalArgumentException("position must be in bounds");

        if (piece.equals(positionMap.get(tile))) return;

        positionMap.put(tile, piece);
    }

    public void removePieceAt(T tile) {
        if (!bounds.contains(tile))
            throw new IllegalArgumentException("position must be in bounds");
        if (!positionMap.containsKey(tile)) return;

        positionMap.remove(tile);
    }

    public void movePiece(T from, T to) {
        if (!bounds.contains(from))
            throw new IllegalArgumentException("The 'from' square " + from + " is outside the bounds " + bounds);
        if (!bounds.contains(to))
            throw new IllegalArgumentException("The 'to' square " + to + " is outside the bounds " + bounds);

        if (from.equals(to)) return;

        ChessPiece<P> piece = positionMap.get(from);
        if (piece == null) return;

        positionMap.remove(from);
        positionMap.put(to, piece);
    }

    public void setAllPieces(Map<T, ChessPiece<P>> newPieces) {
        positionMap = new HashMap<>(newPieces);
    }

    public void removePieces() {
        positionMap.clear();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        MapChessBoard<?, ?> that = (MapChessBoard<?, ?>) object;
        return Objects.equals(positionMap, that.positionMap) && Objects.equals(bounds, that.bounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionMap, bounds);
    }

    @Override
    public String toString() {
        return "MapChessBoard{" +
                "positionMap=" + positionMap +
                ", bounds=" + bounds +
                '}';
    }
}
