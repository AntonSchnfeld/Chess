package org.example;

import org.example.pieces.ChessPiece;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChessBoard implements ChessBoardView {
    private final ChessBoardDimensions dimensions;
    private final Map<Position, ChessPiece> board;

    public ChessBoard(Map<Position, ChessPiece> board, ChessBoardDimensions dimensions) {
        this.board = new HashMap<>(board);
        this.dimensions = dimensions;
    }

    public boolean movePiece(Position start, Position target) {
        ChessPiece piece = board.get(start);
        if (!piece.getValidMoves(this, start).contains(target))
            return false;
        board.remove(start);
        board.put(target, piece);
        return true;
    }

    @Override
    public Map<Position, ChessPiece> getPiecePositions() {
        return Collections.unmodifiableMap(board);
    }

    @Override
    public ChessBoardDimensions getChessBoardDimensions() {
        return dimensions;
    }
}
