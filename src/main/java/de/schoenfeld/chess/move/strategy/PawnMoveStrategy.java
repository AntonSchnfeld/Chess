package de.schoenfeld.chess.move.strategy;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;

import java.io.Serial;

public class PawnMoveStrategy implements MoveStrategy {
    @Serial
    private static final long serialVersionUID = -818103104696833464L;

    @Override
    public MoveCollection getPseudoLegalMoves(ImmutableChessBoard chessBoard, Position pos) {
        MoveCollection moves = new MoveCollection();
        ChessPiece pawn = chessBoard.getPieceAt(pos);
        if (pawn == null) return moves;

        int direction = pawn.isWhite() ? 1 : -1;

        // Forward moves
        Position oneStep = pos.offset(0, direction);
        if (chessBoard.getBounds().contains(oneStep) && chessBoard.getPieceAt(oneStep) == null) {
            moves.add(Move.of(pawn, pos, oneStep));

            if (!pawn.hasMoved()) {
                Position twoStep = oneStep.offset(0, direction);
                if (chessBoard.getBounds().contains(twoStep) && chessBoard.getPieceAt(twoStep) == null) {
                    moves.add(Move.of(pawn, pos, twoStep));
                }
            }
        }

        // Capture moves
        for (int xOffset : new int[]{-1, 1}) {
            Position capturePos = pos.offset(xOffset, direction);
            if (!chessBoard.getBounds().contains(capturePos)) continue;

            ChessPiece target = chessBoard.getPieceAt(capturePos);
            if (target != null && target.isWhite() != pawn.isWhite()) {
                moves.add(Move.of(pawn, pos, capturePos, new CaptureComponent(target)));
            }
        }

        return moves;
    }
}
