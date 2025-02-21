package org.example.pieces;

import org.example.ChessBoardView;
import org.example.MoveCollection;
import org.example.Position;

public class King extends ChessPiece {

    public King(boolean colour) {
        super(colour);
    }

    @Override
    public MoveCollection getValidMoves(ChessBoardView view, Position curPos) {
        return new MoveCollection();
    }
}
