package org.example.pieces;

import org.example.ChessBoardView;
import org.example.Position;

import java.util.List;

public class King extends ChessPiece {

    public King(boolean colour) {
        super(colour);
    }

    @Override
    public List<Position> getValidMoves(ChessBoardView view, Position curPos) {
        return List.of();
    }
}
