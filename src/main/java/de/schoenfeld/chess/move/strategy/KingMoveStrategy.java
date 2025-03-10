package de.schoenfeld.chess.move.strategy;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;

import java.io.Serial;
import java.util.List;

public class KingMoveStrategy implements MoveStrategy {
    private static final List<Position> KING_DIRECTIONS = List.of(
            new Position(1, 0), new Position(-1, 0), // Left & Right
            new Position(0, 1), new Position(0, -1), // Up & Down
            new Position(1, 1), new Position(-1, -1), // Diagonal
            new Position(1, -1), new Position(-1, 1)  // Diagonal
    );
    @Serial
    private static final long serialVersionUID = 1048319777318786255L;

    @Override
    public MoveCollection getPseudoLegalMoves(ImmutableChessBoard chessBoard, Position pos) {
        MoveCollection moves = new MoveCollection();
        ChessPiece king = chessBoard.getPieceAt(pos);

        for (Position direction : KING_DIRECTIONS) {
            Position targetPos = pos.offset(direction);

            // Ensure the move is within board bounds
            if (!chessBoard.getBounds().contains(targetPos)) {
                continue;
            }

            ChessPiece targetPiece = chessBoard.getPieceAt(targetPos);

            if (targetPiece == null)
                moves.add(Move.of(king, pos, targetPos));
            else if (targetPiece.isWhite() != king.isWhite())
                moves.add(Move.of(king, pos, targetPos, new CaptureComponent(targetPiece)));
        }
        return moves;
    }
}
