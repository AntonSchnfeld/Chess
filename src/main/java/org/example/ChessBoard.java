package org.example;

import org.example.pieces.ChessPiece;

import java.util.HashMap;
import java.util.Map;

public class ChessBoard implements ChessBoardView {
    private final ChessBoardBounds dimensions;
    private final Map<Position, ChessPiece> board;
    private final MoveHistory moveHistory;

    public ChessBoard() {
        this(new ChessBoardBounds(8, 8));
    }

    public ChessBoard(ChessBoardBounds dimensions) {
        this(dimensions, new SimpleMoveHistory());
    }

    public ChessBoard(ChessBoardBounds dimensions, MoveHistory moveHistory) {
        this.board = new HashMap<>();
        this.dimensions = dimensions;
        this.moveHistory = moveHistory;
    }

    public void makeMove(Move move) {
        board.remove(move.from());
        board.put(move.to(), move.movedPiece());
        moveHistory.recordMove(move);
    }

    public MoveHistory getMoveHistory() {
        return moveHistory;
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

    @Override
    public Position getKingPosition(boolean colour) {
        return null;
    }
}
