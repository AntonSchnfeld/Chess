package de.schoenfeld.chesskit.model;

import java.io.Serializable;

public record ChessPiece<T extends PieceType>(
        T pieceType,
        Color color
) implements Serializable {
}