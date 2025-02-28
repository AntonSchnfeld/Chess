package de.schoenfeld.chess.logic.piece.strategy;

import de.schoenfeld.chess.data.Position;
import de.schoenfeld.chess.data.ReadOnlyChessBoard;
import de.schoenfeld.chess.data.move.CaptureComponent;
import de.schoenfeld.chess.data.move.Move;
import de.schoenfeld.chess.data.move.MoveCollection;
import de.schoenfeld.chess.logic.piece.ChessPiece;

import java.util.List;

public class KingMoveStrategy implements MoveStrategy {
    private static final List<Position> KING_DIRECTIONS = List.of(
            new Position(1, 0), new Position(-1, 0), // Left & Right
            new Position(0, 1), new Position(0, -1), // Up & Down
            new Position(1, 1), new Position(-1, -1), // Diagonal
            new Position(1, -1), new Position(-1, 1)  // Diagonal
    );

    @Override
    public MoveCollection getPseudoLegalMoves(ReadOnlyChessBoard chessBoard, Position pos) {
        MoveCollection moves = new MoveCollection();
        ChessPiece king = chessBoard.getPieceAt(pos);

        if (king == null) return moves; // Safety check

        for (Position direction : KING_DIRECTIONS) {
            Position targetPos = pos.offset(direction);

            // Ensure the move is within board bounds
            if (!chessBoard.getBounds().contains(targetPos)) {
                continue;
            }

            ChessPiece targetPiece = chessBoard.getPieceAt(targetPos);

            // Add move only if target square is empty or occupied by an opponent
            if (targetPiece == null || targetPiece.isWhite() != king.isWhite()) {
                moves.add(Move.of(king, pos, targetPos, new CaptureComponent(targetPiece)));
            }
        }
        return moves;
    }
}
