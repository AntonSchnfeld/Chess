package org.example.pieces;

import org.example.ChessBoardView;
import org.example.Position;

import java.util.LinkedList;
import java.util.List;

public class Pawn extends ChessPiece {

    public Pawn(boolean colour) {
        super(colour);
    }

    @Override
    public List<Position> getValidMoves(ChessBoardView view, Position curPos) {
        List<Position> moves = new LinkedList<>();

        return moves;
    }
}
