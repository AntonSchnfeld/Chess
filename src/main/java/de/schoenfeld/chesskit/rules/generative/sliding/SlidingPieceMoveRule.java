package de.schoenfeld.chesskit.rules.generative.sliding;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.Square;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.CaptureComponent;
import de.schoenfeld.chesskit.rules.generative.AbstractGenerativeMoveRule;

import java.util.List;

public abstract class SlidingPieceMoveRule<T extends PieceType> extends AbstractGenerativeMoveRule<T> {

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

    public SlidingPieceMoveRule(T type, List<Square> directions) {
        super(type);
        this.directions = directions;
    }

    @Override
    protected void generatePieceMoves(GameState<T> gameState,
                                      Square position,
                                      MoveLookup<T> moves) {
        ChessBoard<T> board = gameState.getChessBoard();
        ChessPiece<T> piece = gameState.getPieceAt(position);

        for (Square direction : directions) {
            Square current = position.offset(direction);

            while (board.getBounds().contains(current)) {
                ChessPiece<T> target = board.getPieceAt(current);

                if (target != null) {
                    // If target is an opponent's piece, add a capturing move
                    moves.add(Move.of(piece, position, current, new CaptureComponent<T>(target)));
                    break;
                }

                // Add normal move if the square is empty
                moves.add(Move.of(piece, position, current));
                current = current.offset(direction);
            }
        }
    }
}
