package de.schoenfeld.chess;

import de.schoenfeld.chess.pieces.ChessPiece;

public record Move(Position from, Position to, ChessPiece movedPiece, ChessPiece capturedPiece, boolean isCastle) {
    public static Move of(Position from, Position to, ChessPiece movedPiece, ChessPiece capturedPiece, boolean isCastle) {
        return new Move(from, to, movedPiece, capturedPiece, isCastle);
    }

    public static Move normal(Position from, Position to, ChessPiece movedPiece) {
        return new Move(from, to, movedPiece, null, false);
    }
}
