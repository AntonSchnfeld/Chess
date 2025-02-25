package de.schoenfeld.chess.pieces;

import de.schoenfeld.chess.ChessBoardView;
import de.schoenfeld.chess.MoveCollection;
import de.schoenfeld.chess.Position;

public class King extends ChessPiece {

    public King(boolean colour) {
        super(colour);
    }

    @Override
    public MoveCollection getValidMoves(ChessBoardView view, Position curPos) {
        return new MoveCollection();
    }
}
