package de.schoenfeld.chess.pieces;

import de.schoenfeld.chess.ChessBoardView;
import de.schoenfeld.chess.Move;
import de.schoenfeld.chess.MoveCollection;
import de.schoenfeld.chess.Position;

public class Bishop extends ChessPiece {

    public Bishop(boolean colour) {
        super(colour);
    }

    @Override
    public MoveCollection getValidMoves(ChessBoardView view, Position curPos) {
        MoveCollection validMoves = new MoveCollection();

        int[][] directions = {
                {1, 1},
                {-1, 1},
                {1, -1},
                {-1, -1}
        };

        for (int[] direction : directions) {
            Position nextPos = curPos.offset(direction[0], direction[1]);

            while (view.getChessBoardBounds().contains(nextPos)) {
                ChessPiece piece = view.getPieceAt(nextPos);
                if (piece != null && piece.getColour() != colour) {
                    validMoves.add(Move.of(curPos, nextPos, this, piece, false));
                    break;
                } else if (piece == null)
                    validMoves.add(Move.of(curPos, nextPos, this, null, false));
                else break;
                nextPos = nextPos.offset(direction[0], direction[1]);
            }
        }

        return validMoves;
    }
}
