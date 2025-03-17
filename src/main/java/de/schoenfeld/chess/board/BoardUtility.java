package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class BoardUtility {
    private BoardUtility() {
    }

    public static ImmutableChessBoard fromFen(String fen) {
        Map<Position, ChessPiece> positions = new HashMap<>();
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
                            new Position(x, y),
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

        return new MapChessBoard(
                Collections.unmodifiableMap(positions),
                new ChessBoardBounds(8, 8)
        );
    }

    private static ChessPiece createPieceFromFenChar(char c) {
        boolean isWhite = Character.isUpperCase(c);
        char pieceChar = Character.toLowerCase(c);

        PieceType type = switch (pieceChar) {
            case 'p' -> PieceType.PAWN;
            case 'n' -> PieceType.KNIGHT;
            case 'b' -> PieceType.BISHOP;
            case 'r' -> PieceType.ROOK;
            case 'q' -> PieceType.QUEEN;
            case 'k' -> PieceType.KING;
            default -> throw new IllegalArgumentException(
                    "Invalid FEN character: " + c
            );
        };

        return new ChessPiece(type, isWhite);
    }

    public static ImmutableChessBoard getDefaultBoard() {
        return fromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
    }

    public static GameState fromPgn(String pgn) {
        // Implementation omitted
        return null;
    }
}