package org.example;

import org.example.pieces.*;

public class ChessBoardFactory {
    private ChessBoardFactory() {

    }

    public static ChessBoard createDefaultChessBoard() {
        return createChessBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public static ChessBoard createChessBoardFromFEN(String fen) {
        // Split the FEN string into its components
        String[] parts = fen.split(" ");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid FEN string.");
        }

        // Piece placement part (first part of the FEN string)
        String piecePlacement = parts[0];
        // Active color (whites or blacks turn)
        String activeColor = parts[1];
        // Castling availability
        String castlingAvailability = parts[2];
        // En passant target square (can be empty)
        String enPassantTarget = parts[3];

        // Initialize the chess board with an empty map of positions
        ChessBoardBounds dimensions = new ChessBoardBounds(8, 8);
        ChessBoard chessBoard = new ChessBoard(dimensions);

        // Parse the piece placement
        String[] rows = piecePlacement.split("/");
        if (rows.length != 8) {
            throw new IllegalArgumentException("Invalid FEN: There should be 8 rows of piece placements.");
        }

        for (int row = 0; row < 8; row++) {
            String rowData = rows[row];
            int col = 0;

            for (char c : rowData.toCharArray()) {
                Position position = new Position(col, 7 - row);  // In FEN, row 8 is at the bottom
                ChessPiece piece;

                if (Character.isDigit(c)) {
                    // Skip empty squares (numbers represent consecutive empty squares)
                    col += Character.getNumericValue(c);
                } else {
                    piece = createPiece(c);  // Create the piece based on the character
                    chessBoard.putChessPiece(position, piece);
                    col++;
                }
            }
        }

        return chessBoard;
    }

    private static ChessPiece createPiece(char pieceChar) {
        // Here you should map the characters to actual pieces like Rook, Pawn, etc.
        return switch (pieceChar) {
            case 'K' -> new King(true); // White King
            case 'k' -> new King(false); // Black King
            case 'Q' -> new Queen(true); // White Queen
            case 'q' -> new Queen(false); // Black Queen
            case 'R' -> new Rook(true); // White Rook
            case 'r' -> new Rook(false); // Black Rook
            case 'B' -> new Bishop(true); // White Bishop
            case 'b' -> new Bishop(false); // Black Bishop
            case 'N' -> new Knight(true); // White Knight
            case 'n' -> new Knight(false); // Black Knight
            case 'P' -> new Pawn(true); // White Pawn
            case 'p' -> new Pawn(false); // Black Pawn
            default -> throw new IllegalArgumentException("Invalid piece character: " + pieceChar);
        };
    }
}
