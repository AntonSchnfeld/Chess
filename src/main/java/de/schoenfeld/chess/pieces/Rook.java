package de.schoenfeld.chess.pieces;

import de.schoenfeld.chess.ChessBoardView;
import de.schoenfeld.chess.MoveCollection;
import de.schoenfeld.chess.Position;

public class Rook extends ChessPiece {

    public Rook(boolean colour) {
        super(colour);
    }

    @Override
    public MoveCollection getValidMoves(ChessBoardView view, Position curPos) {
        return new MoveCollection();
    }
}
