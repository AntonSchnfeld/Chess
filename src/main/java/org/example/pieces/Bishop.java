package org.example.pieces;

import org.example.ChessBoardView;
import org.example.Position;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends ChessPiece {

    public Bishop(boolean colour) {
        super(colour);
    }

    @Override
    public List<Position> getValidMoves(ChessBoardView view, Position curPos) {
        List<Position> validMoves = new ArrayList<>();

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
                    validMoves.add(nextPos);
                    break;
                }
                else if (piece == null)
                    validMoves.add(nextPos);
                else break;
                nextPos = nextPos.offset(direction[0], direction[1]);
            }
        }

        return validMoves;
    }
}
