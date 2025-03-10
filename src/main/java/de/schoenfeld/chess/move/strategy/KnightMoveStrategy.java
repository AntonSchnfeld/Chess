package de.schoenfeld.chess.move.strategy;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;

import java.io.Serial;

public class KnightMoveStrategy implements MoveStrategy {
    private static final int[][] KNIGHT_MOVES = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };
    @Serial
    private static final long serialVersionUID = -3638622122729577291L;

    @Override
    public MoveCollection getPseudoLegalMoves(ImmutableChessBoard chessBoard, Position pos) {
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