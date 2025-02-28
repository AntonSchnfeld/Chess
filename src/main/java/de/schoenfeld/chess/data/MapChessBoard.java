package de.schoenfeld.chess.data;

import de.schoenfeld.chess.logic.piece.ChessPiece;
import de.schoenfeld.chess.logic.piece.PieceType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapChessBoard implements ChessBoard {
    private final Map<Position, ChessPiece> positionChessPieceMap;
    private final Map<ChessPiece, Position> chessPiecePositionMap;
    private final ChessBoardBounds bounds;

    public MapChessBoard(Map<Position, ChessPiece> positionChessPieceMap,
                         ChessBoardBounds bounds) {
        this.positionChessPieceMap = positionChessPieceMap;
        this.chessPiecePositionMap = new HashMap<>();
        for (var entry : positionChessPieceMap.entrySet())
            chessPiecePositionMap.put(entry.getValue(), entry.getKey());
        this.bounds = bounds;
    }

    public MapChessBoard(ChessBoardBounds bounds) {
        this(new HashMap<>(), bounds);
    }

    public MapChessBoard() {
        this(new HashMap<>(), new ChessBoardBounds(8, 8));
    }

    public ChessPiece getPieceAt(Position position) {
        return positionChessPieceMap.get(position);
    }

    @Override
    public ChessBoardBounds getBounds() {
        return bounds;
    }

    @Override
    public List<ChessPiece> getPieces(boolean isWhite) {
        return positionChessPieceMap
                .values()
                .stream()
                .filter(ChessPiece::isWhite)
                .toList();
    }

    @Override
    public Position getPiecePosition(ChessPiece chessPiece) {
        return chessPiecePositionMap.get(chessPiece);
    }

    public void setPiece(ChessPiece piece, Position position) {
        positionChessPieceMap.put(position, piece);
    }

    @Override
    public void removePieceAt(Position position) {
        positionChessPieceMap.remove(position);
    }

    @Override
    public List<ChessPiece> getPiecesOfType(PieceType pieceType, boolean colour) {
        return positionChessPieceMap.values().stream()
                .filter(piece ->
                        piece.getPieceType().equals(pieceType) &&
                                piece.isWhite() == colour)
                .toList();
    }

    @Override
    public String toFen() {
        return "";
    }
}
