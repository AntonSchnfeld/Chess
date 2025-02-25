package de.schoenfeld.chess.pieces;

import de.schoenfeld.chess.ChessBoardView;
import de.schoenfeld.chess.Move;
import de.schoenfeld.chess.MoveCollection;
import de.schoenfeld.chess.Position;

public class Knight extends ChessPiece {

    private static final int[][] moveOffsets = {
            {1, 2},
            {2, 1},
            {-1, 2},
            {2, -1},
            {1, -2},
            {-2, 1},
            {-1, -2},
            {-2, -1}
    };

    public Knight(boolean colour) {
        super(colour);
    }

    @Override
    public MoveCollection getValidMoves(ChessBoardView view, Position curPos) {
        MoveCollection validMoves = new MoveCollection();

        for (int[] offsets : moveOffsets) {
            // Calculate position to investigate
            int testPosRow = curPos.x() + offsets[0];
            int testPosCol = curPos.y() + offsets[1];
            Position testPosition = new Position(testPosRow, testPosCol);

            // Don't add out of bounds moves
            if (!view.getChessBoardBounds().contains(testPosition)) continue;
            // Don't add moves which would capture friendly pieces
            ChessPiece testPositionPiece = view.getPieceAt(testPosition);
            if (testPositionPiece != null && testPositionPiece.getColour() == colour) continue;

            validMoves.add(Move.of(curPos, testPosition, this, testPositionPiece, false));
        }

        return validMoves;
    }
}
