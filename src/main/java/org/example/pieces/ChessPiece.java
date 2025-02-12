package org.example.pieces;

import org.example.ChessBoardView;
import org.example.Position;

import java.util.List;

public abstract class ChessPiece {
    protected final boolean colour;

    public ChessPiece(boolean colour) {
        this.colour = colour;
    }

    public abstract List<Position> getValidMoves(ChessBoardView view, Position curPos);

    public boolean getColour() {
        return colour;
    }
}