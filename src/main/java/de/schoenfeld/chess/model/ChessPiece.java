package de.schoenfeld.chess.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class ChessPiece implements Serializable {
    @Serial
    private static final long serialVersionUID = 3459462864331349341L;

    private final PieceType pieceType;
    private final boolean colour;
    private boolean moved;

    public ChessPiece(PieceType pieceType, boolean isWhite) {
        this.pieceType = pieceType;
        this.moved = false;
        this.colour = isWhite;
    }

    public boolean isWhite() {
        return colour;
    }

    public boolean hasMoved() {
        return moved;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setMoved() {
        this.moved = true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, colour, moved);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceType=" + pieceType +
                ", colour=" + colour +
                ", moved=" + moved +
                '}';
    }
}
