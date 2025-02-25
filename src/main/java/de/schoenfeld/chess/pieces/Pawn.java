package de.schoenfeld.chess.pieces;

import de.schoenfeld.chess.*;

public class Pawn extends ChessPiece {
    private boolean moved;

    public Pawn(boolean colour) {
        super(colour);
        this.moved = false;
    }

    public boolean hasMoved() {
        return moved;
    }

    @Override
    public MoveCollection getValidMoves(ChessBoardView view, Position curPos) {
        // TODO: En passant and promotion

        MoveCollection moves = new MoveCollection();
        int direction = colour ? 1 : -1;
        ChessBoardBounds bounds = view.getChessBoardBounds();

        Position inFront = curPos.offset(0, direction);
        if (bounds.contains(inFront) && view.getPieceAt(inFront) == null) {
            moves.add(Move.normal(curPos, inFront, this));
            Position twoInFront = inFront.offset(0, direction);
            if (bounds.contains(twoInFront) && view.getPieceAt(twoInFront) == null && !moved)
                moves.add(Move.normal(curPos, twoInFront, this));
        }

        Position leftDiagonal = curPos.offset(-1, direction);
        ChessPiece leftDiagonalPiece = view.getPieceAt(leftDiagonal);
        if (leftDiagonalPiece != null && leftDiagonalPiece.getColour() != colour)
            moves.add(Move.of(curPos, leftDiagonal, this, leftDiagonalPiece, false));

        Position rightDiagonal = curPos.offset(1, direction);
        ChessPiece rightDiagonalPiece = view.getPieceAt(rightDiagonal);
        if (rightDiagonalPiece != null && rightDiagonalPiece.getColour() != colour)
            moves.add(Move.of(curPos, rightDiagonal, this, rightDiagonalPiece, false));

        return moves;
    }
}
