package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record MapChessBoard(
        Map<Position, ChessPiece> positionMap,
        Map<ChessPiece, Position> chessPieceMap,
        ChessBoardBounds bounds
) implements ImmutableChessBoard {

    public MapChessBoard {
        positionMap = Map.copyOf(positionMap);
        chessPieceMap = Map.copyOf(chessPieceMap);
    }

    public MapChessBoard(Map<Position, ChessPiece> positionMap, ChessBoardBounds bounds) {
        this(positionMap, positionMap.entrySet().stream().
                        collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)),
                bounds);
    }

    public MapChessBoard(ChessBoardBounds chessBoardBounds) {
        this(Map.of(), chessBoardBounds);
    }

    @Override
    public ChessPiece getPieceAt(Position position) {
        return positionMap.get(position);
    }

    @Override
    public Position getPiecePosition(ChessPiece chessPiece) {
        return chessPieceMap.get(chessPiece);
    }

    @Override
    public ChessBoardBounds getBounds() {
        return bounds;
    }

    @Override
    public List<ChessPiece> getPiecesOfColour(boolean isWhite) {
        return chessPieceMap.keySet().stream()
                .filter(ChessPiece::isWhite)
                .toList();
    }

    @Override
    public List<ChessPiece> getPieces() {
        return chessPieceMap.keySet().stream().toList();
    }

    @Override
    public List<ChessPiece> getPiecesOfType(PieceType pieceType, boolean isWhite) {
        return chessPieceMap.keySet().stream()
                .filter(p -> p.isWhite() && p.getPieceType().equals(pieceType))
                .toList();
    }

    public MapChessBoard withPieceAt(ChessPiece piece, Position position) {
        if (!bounds.contains(position))
            throw new IllegalArgumentException("position must be in bounds");
        Map<Position, ChessPiece> newPositions = new HashMap<>(positionMap);
        newPositions.put(position, piece);
        return new MapChessBoard(newPositions, bounds);
    }

    public MapChessBoard withoutPieceAt(Position position) {
        Map<Position, ChessPiece> newPositions = new HashMap<>(positionMap);
        newPositions.remove(position);
        return new MapChessBoard(newPositions, bounds);
    }

    public MapChessBoard withPieceMoved(Position from, Position to) {
        ChessPiece piece = getPieceAt(from);
        return withoutPieceAt(from).withPieceAt(piece, to);
    }

    public MapChessBoard withAllPieces(Map<Position, ChessPiece> newPieces) {
        return new MapChessBoard(newPieces, bounds);
    }

    public MapChessBoard withoutPieces() {
        return new MapChessBoard(Map.of(), bounds);
    }

    public MapChessBoard withBounds(ChessBoardBounds newBounds) {
        return new MapChessBoard(positionMap, newBounds);
    }

    @Override
    public String toFen() {
        return "";
    }
}
