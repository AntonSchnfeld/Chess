package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record MapChessBoard<T extends PieceType>(
        Map<Square, ChessPiece> positionMap,
        Map<ChessPiece, Square> chessPieceMap,
        ChessBoardBounds bounds
) implements ChessBoard<T> {

    public MapChessBoard {
        positionMap = Map.copyOf(positionMap);
        chessPieceMap = Map.copyOf(chessPieceMap);
    }

    public MapChessBoard(Map<Square, ChessPiece> positionMap, ChessBoardBounds bounds) {
        this(positionMap, positionMap.entrySet().stream().
                        collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)),
                bounds);
    }

    public MapChessBoard(ChessBoardBounds chessBoardBounds) {
        this(Map.of(), chessBoardBounds);
    }

    @Override
    public ChessPiece getPieceAt(Square square) {
        return positionMap.get(square);
    }

    @Override
    public Square getPiecePosition(ChessPiece chessPiece) {
        return chessPieceMap.get(chessPiece);
    }

    @Override
    public ChessBoardBounds getBounds() {
        return bounds;
    }

    @Override
    public List<ChessPiece> getPiecesOfColour(boolean isWhite) {
        List<ChessPiece> pieces = new ArrayList<>(chessPieceMap.size() / 2);
        for (Map.Entry<ChessPiece, Square> entry : chessPieceMap.entrySet()) {
            if (entry.getKey().isWhite() == isWhite) {
                pieces.add(entry.getKey());
            }
        }
        return pieces;
    }

    @Override
    public List<ChessPiece> getPieces() {
        return chessPieceMap.keySet().stream().toList();
    }

    @Override
    public List<ChessPiece> getPiecesOfTypeAndColour(T pieceType, boolean isWhite) {
        List<ChessPiece> pieces = new ArrayList<>((chessPieceMap.size() / 2) / 8);
        for (Map.Entry<ChessPiece, Square> entry : chessPieceMap.entrySet()) {
            if (entry.getKey().isWhite() == isWhite && entry.getKey().pieceType().equals(pieceType)) {
                pieces.add(entry.getKey());
            }
        }
        return pieces;
    }

    @Override
    public List<ChessPiece> getPiecesOfType(T pieceType) {
        List<ChessPiece> pieces = new ArrayList<>(chessPieceMap.size() / 8);
        for (Map.Entry<ChessPiece, Square> entry : chessPieceMap.entrySet()) {
            if (entry.getKey().pieceType().equals(pieceType)) {
                pieces.add(entry.getKey());
            }
        }
        return pieces;
    }

    public MapChessBoard<T> withPieceAt(ChessPiece piece, Square square) {
        if (!bounds.contains(square))
            throw new IllegalArgumentException("position must be in bounds");

        if (square.equals(chessPieceMap.get(piece))) return this;

        Map<Square, ChessPiece> newPositions = new HashMap<>(positionMap);
        newPositions.put(square, piece);
        return new MapChessBoard<>(newPositions, bounds);
    }

    public MapChessBoard<T> withoutPieceAt(Square square) {
        if (!bounds.contains(square))
            throw new IllegalArgumentException("position must be in bounds");
        if (!positionMap.containsKey(square)) return this;

        Map<Square, ChessPiece> newPositions = new HashMap<>(positionMap);
        newPositions.remove(square);
        return new MapChessBoard<>(newPositions, bounds);
    }

    public MapChessBoard<T> withPieceMoved(Square from, Square to) {
        if (from.equals(to)) return this;
        ChessPiece piece = getPieceAt(from);
        if (piece == null) return this;
        return withoutPieceAt(from).withPieceAt(piece.withMoved(true), to);
    }

    public MapChessBoard<T> withAllPieces(Map<Square, ChessPiece> newPieces) {
        if (newPieces.isEmpty()) return this;
        return new MapChessBoard<>(newPieces, bounds);
    }

    public MapChessBoard<T> withoutPieces() {
        return new MapChessBoard<>(Map.of(), Map.of(), bounds);
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
