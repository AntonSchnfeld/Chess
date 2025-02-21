package org.example.pieces;

import org.example.ChessBoardView;
import org.example.MoveCollection;
import org.example.Position;

public abstract class ChessPiece {
    protected final boolean colour;

    public ChessPiece(boolean colour) {
        this.colour = colour;
    }

    public abstract MoveCollection getValidMoves(ChessBoardView view, Position curPos);

    public boolean getColour() {
        return colour;
    }
}