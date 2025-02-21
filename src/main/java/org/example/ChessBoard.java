package org.example;

import org.example.pieces.ChessPiece;
import org.example.pieces.King;

import java.util.HashMap;
import java.util.Map;

public class ChessBoard implements ChessBoardView {
    private final ChessBoardBounds dimensions;
    private final Map<Position, ChessPiece> board;

    public ChessBoard(ChessBoardBounds dimensions) {
        this.board = new HashMap<>();
        this.dimensions = dimensions;
    }

    public void makeMove(Move move) {
        board.remove(move.from());
        board.put(move.to(), move.movedPiece());
    }

    @Override
    public ChessPiece getPieceAt(Position position) {
        return board.get(position);
    }

    public void putChessPiece(Position position, ChessPiece piece) {
        board.put(position, piece);
    }

    @Override
    public ChessBoardBounds getChessBoardBounds() {
        return dimensions;
    }
}
