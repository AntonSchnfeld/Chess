package org.example.pieces;

import org.example.ChessBoard;
import org.example.ChessBoardDimensions;
import org.example.Position;

import java.util.HashMap;
import java.util.Map;

public class KnightTest {
    public void testKnightMoveOnEmptyBoard() {
        Map<Position, ChessPiece> board = new HashMap<>();
        Knight knight = new Knight(true);
        board.put(new Position(3, 3), knight);

        ChessBoard emptyBoard = new ChessBoard(board, new ChessBoardDimensions(8, 8));


    }
}
