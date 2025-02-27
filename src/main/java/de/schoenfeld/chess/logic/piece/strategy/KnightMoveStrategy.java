package de.schoenfeld.chess.logic.piece.strategy;

import de.schoenfeld.chess.Position;
import de.schoenfeld.chess.data.ReadOnlyChessBoard;
import de.schoenfeld.chess.data.move.CaptureComponent;
import de.schoenfeld.chess.data.move.Move;
import de.schoenfeld.chess.data.move.MoveCollection;
import de.schoenfeld.chess.logic.piece.ChessPiece;

public class KnightMoveStrategy implements MoveStrategy {
    private static final int[][] KNIGHT_MOVES = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };

    @Override
    public MoveCollection getPseudoLegalMoves(ReadOnlyChessBoard chessBoard, Position pos) {
        MoveCollection moves = new MoveCollection();
        ChessPiece piece = chessBoard.getPieceAt(pos);
        if (piece == null) return moves;

        for (int[] move : KNIGHT_MOVES) {
            Position newPos = pos.offset(move[0], move[1]);
            if (!chessBoard.getBounds().contains(newPos)) continue;

            ChessPiece targetPiece = chessBoard.getPieceAt(newPos);
            if (targetPiece != null && targetPiece.isWhite() != piece.isWhite())
                    moves.add(Move.of(piece, pos, newPos, new CaptureComponent(targetPiece)));
            else moves.add(Move.of(piece, pos, newPos));
        }
        return moves;
    }
}