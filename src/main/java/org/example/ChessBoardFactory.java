package org.example;

import org.example.pieces.*;

import java.util.HashMap;
import java.util.Map;

public class ChessBoardFactory {
    private ChessBoardFactory() {

    }

    public static ChessBoard createDefaultChessBoard() {
        Map<Position, ChessPiece> board = new HashMap<>();

        for (int i = 0; i < 8; i++) {
            board.put(new Position(i, 1), new Pawn(true));
            board.put(new Position(i, 6), new Pawn(false));
        }

        board.put(new Position(0, 0), new Rook(true));
        board.put(new Position(7, 0), new Rook(true));
        board.put(new Position(0, 7), new Rook(false));
        board.put(new Position(7, 7), new Rook(false));

        board.put(new Position(1, 0), new Knight(true));
        board.put(new Position(6, 0), new Knight(true));
        board.put(new Position(1, 7), new Knight(false));
        board.put(new Position(6, 7), new Knight(false));

        board.put(new Position(2, 0), new Bishop(true));
        board.put(new Position(5, 0), new Bishop(true));
        board.put(new Position(2, 7), new Bishop(false));
        board.put(new Position(5, 7), new Bishop(false));

        board.put(new Position(3, 0), new Queen(true));
        board.put(new Position(3, 7), new Queen(true));

        board.put(new Position(4, 0), new King(true));
        board.put(new Position(4, 7), new King(false));

        return new ChessBoard(board, new ChessBoardDimensions(7, 7));
    }

    public static ChessBoard createChessBoardFromPGN(String pgn) {
        Map<Position, ChessPiece> board = new HashMap<>();

        return new ChessBoard(board, new ChessBoardDimensions(8, 8));
    }
}
