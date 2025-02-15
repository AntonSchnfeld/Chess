package org.example;

import org.example.pieces.ChessPiece;

import java.util.HashMap;
import java.util.Map;

public class ChessBoard implements ChessBoardView {
    private final ChessBoardBounds dimensions;
    private final Map<Position, ChessPiece> board;

    public ChessBoard(ChessBoardBounds dimensions) {
        this.board = new HashMap<>();
        this.dimensions = dimensions;
    }

    public boolean movePiece(Position startPos, Position targetPos) {
        ChessPiece piece = board.get(startPos);
        if (!piece.getValidMoves(this, startPos).contains(targetPos))
            return false;
        board.put(targetPos, piece);
        board.remove(startPos);
        return true;
    }

    @Override
    public ChessPiece getPieceAt(Position position) {
        return board.get(position);
    }

    public void putChessPiece(Position position, ChessPiece piece) {
        board.put(position, piece);
    }

    public void executeMove(Move move) {
    }

    @Override
    public ChessBoardBounds getChessBoardBounds() {
        return dimensions;
    }
}
