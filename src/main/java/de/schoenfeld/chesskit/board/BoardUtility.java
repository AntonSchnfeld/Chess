package de.schoenfeld.chesskit.board;

import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.*;

import java.util.HashMap;
import java.util.Map;

import static de.schoenfeld.chesskit.model.StandardPieceType.*;

public final class BoardUtility {
    private BoardUtility() {
    }

    public static ChessBoard<Square8x8, StandardPieceType> fromFen(String fen) {
        Map<Square8x8, ChessPiece<StandardPieceType>> positions = new HashMap<>();
        String[] fenParts = fen.split("\\s+");
        String[] ranks = fenParts[0].split("/");

        if (ranks.length != 8) {
            throw new IllegalArgumentException("Invalid FEN: Requires 8 ranks");
        }

        for (int rankIndex = 0; rankIndex < 8; rankIndex++) {
            int y = 7 - rankIndex; // Convert FEN rank to y-coordinate
            int x = 0;

            for (char c : ranks[rankIndex].toCharArray()) {
                if (Character.isDigit(c)) {
                    x += Character.getNumericValue(c);
                } else {
                    positions.put(
                            Square8x8.of(x, y),
                            createPieceFromFenChar(c)
                    );
                    x++;
                }

                if (x > 8) throw new IllegalArgumentException(
                        "Rank overflow in FEN: " + ranks[rankIndex]
                );
            }

            if (x != 8) throw new IllegalArgumentException(
                    "Invalid rank length in FEN: " + ranks[rankIndex]
            );
        }

        return new MapChessBoard<>(positions, new Square8x8ChessBoardBounds());
    }

    private static ChessPiece<StandardPieceType> createPieceFromFenChar(char c) {
        Color isWhite = Character.isUpperCase(c) ? Color.WHITE : Color.BLACK;
        char pieceChar = Character.toLowerCase(c);

        StandardPieceType type = switch (pieceChar) {
            case 'p' -> PAWN;
            case 'n' -> KNIGHT;
            case 'b' -> BISHOP;
            case 'r' -> ROOK;
            case 'q' -> QUEEN;
            case 'k' -> KING;
            default -> throw new IllegalArgumentException(
                    "Invalid FEN character: " + c
            );
        };

        return new ChessPiece<>(type, isWhite);
    }

    public static ChessBoard<Square8x8, StandardPieceType> getDefaultBoard() {
        return fromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
    }
}