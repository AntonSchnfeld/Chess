package org.example.pieces;

import org.example.ChessBoardView;
import org.example.MoveCollection;
import org.example.Position;

public class Rook extends ChessPiece {

    public Rook(boolean colour) {
        super(colour);
    }

    @Override
    public MoveCollection getValidMoves(ChessBoardView view, Position curPos) {
        return new MoveCollection();
    }
}
