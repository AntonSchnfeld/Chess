package de.schoenfeld.chess.logic.piece;

public class ChessPiece {
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
}
