package org.example;


import org.example.pieces.Bishop;

public class Main {
    public static void main(String[] args) {
        Bishop bishop = new Bishop(true);
        Position pos = new Position(3, 3);
        ChessBoard board = new ChessBoard(new ChessBoardBounds(3, 3));
        board.putChessPiece(pos, bishop);
        long startTime = System.nanoTime();
        for (int i = 0; i < 1_000_000; i++) {
            bishop.getValidMoves(board, pos);
        }
        long elapsedTime = System.nanoTime() - startTime;
        double elapsedMillis = elapsedTime / 1_000_000.0;
        System.out.printf("Elapsed time: %.3f ms%n", elapsedMillis);

        ChessBoard chessBoard = ChessBoardFactory.createDefaultChessBoard();
    }
}