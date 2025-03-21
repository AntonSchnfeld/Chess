package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;
import de.schoenfeld.chess.rules.generative.GenerativeMoveRule;

import java.util.List;

public abstract class SlidingPieceMoveRule implements GenerativeMoveRule {

    public static final List<Square> STRAIGHT_DIRECTIONS = List.of(
            Square.of(1, 0),  // Right
            Square.of(0, 1),  // Up
            Square.of(-1, 0), // Left
            Square.of(0, -1)  // Down
    );

    public static final List<Square> DIAGONAL_DIRECTIONS = List.of(
            Square.of(1, 1),   // Up-right
            Square.of(-1, -1), // Down-left
            Square.of(-1, 1),  // Up-left
            Square.of(1, -1)   // Down-right
    );

    public static final List<Square> ALL_DIRECTIONS = List.of(
            Square.of(1, 1), Square.of(-1, -1),
            Square.of(-1, 1), Square.of(1, -1),

            Square.of(1, 0), Square.of(0, 1),
            Square.of(-1, 0), Square.of(0, -1)
    );

    private final List<Square> directions;

    public SlidingPieceMoveRule(List<Square> directions) {
        this.directions = directions;
    }

    protected void generateMoves(GameState gameState,
                                 ChessPiece piece,
                                 MoveCollection moves) {
        var board = gameState.chessBoard();
        var position = board.getPiecePosition(piece);

        for (var direction : directions) {
            var current = position.offset(direction);

            while (board.getBounds().contains(current)) {
                ChessPiece target = board.getPieceAt(current);

                if (target != null) {
                    // If target is an opponent's piece, add a capturing move
                    moves.add(Move.of(piece, position, current, new CaptureComponent(target)));
                    break;
                }

                // Add normal move if the square is empty
                moves.add(Move.of(piece, position, current));
                current = current.offset(direction);
            }
        }
    }
}
