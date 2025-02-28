package de.schoenfeld.chess.logic.piece.strategy;

import de.schoenfeld.chess.data.Position;
import de.schoenfeld.chess.data.ReadOnlyChessBoard;
import de.schoenfeld.chess.data.move.CaptureComponent;
import de.schoenfeld.chess.data.move.Move;
import de.schoenfeld.chess.data.move.MoveCollection;
import de.schoenfeld.chess.logic.piece.ChessPiece;

import java.util.ArrayList;
import java.util.List;

public class SlidingMoveStrategy implements MoveStrategy {
    public static final List<Position> STRAIGHT_DIRECTIONS = List.of(
            new Position(1, 0),
            new Position(0, 1),
            new Position(-1, 0),
            new Position(0, -1)
    );
    public static final List<Position> DIAGONAL_DIRECTIONS = List.of(
            new Position(1, 1),
            new Position(-1, -1),
            new Position(-1, 1),
            new Position(1, -1)
    );
    public static final List<Position> ALL_DIRECTIONS = new ArrayList<>();

    static {
        ALL_DIRECTIONS.addAll(DIAGONAL_DIRECTIONS);
        ALL_DIRECTIONS.addAll(STRAIGHT_DIRECTIONS);
    }

    private final List<Position> directions;

    public SlidingMoveStrategy(List<Position> directions) {
        this.directions = directions;
    }

    @Override
    public MoveCollection getPseudoLegalMoves(ReadOnlyChessBoard chessBoard, Position pos) {
        MoveCollection moves = new MoveCollection();
        ChessPiece piece = chessBoard.getPieceAt(pos);

        if (piece == null) return moves; // Safety check

        for (Position direction : directions) {
            Position current = pos.offset(direction);

            while (chessBoard.getBounds().contains(current)) {
                ChessPiece target = chessBoard.getPieceAt(current);

                if (target != null) {
                    if (target.isWhite() != piece.isWhite()) {
                        moves.add(Move.of(piece, pos, current, new CaptureComponent(target))); // Capture move
                    }
                    break; // Blocked by any piece
                }

                moves.add(Move.of(piece, pos, current)); // Normal move
                current = current.offset(direction);
            }
        }
        return moves;
    }
}
