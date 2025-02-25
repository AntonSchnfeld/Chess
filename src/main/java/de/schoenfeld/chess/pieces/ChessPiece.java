package de.schoenfeld.chess.pieces;

import de.schoenfeld.chess.ChessBoardView;
import de.schoenfeld.chess.MoveCollection;
import de.schoenfeld.chess.Position;

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