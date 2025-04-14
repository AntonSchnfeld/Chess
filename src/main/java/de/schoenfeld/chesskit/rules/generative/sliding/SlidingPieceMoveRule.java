package de.schoenfeld.chesskit.rules.generative.sliding;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.CaptureComponent;
import de.schoenfeld.chesskit.rules.generative.AbstractGenerativeMoveRule;

import java.util.List;

public abstract class SlidingPieceMoveRule<P extends PieceType> extends AbstractGenerativeMoveRule<Square8x8, P> {

    public static final List<Square8x8> STRAIGHT_DIRECTIONS = List.of(
            Square8x8.of(1, 0),  // Right
            Square8x8.of(0, 1),  // Up
            Square8x8.of(-1, 0), // Left
            Square8x8.of(0, -1)  // Down
    );

    public static final List<Square8x8> DIAGONAL_DIRECTIONS = List.of(
            Square8x8.of(1, 1),   // Up-right
            Square8x8.of(-1, -1), // Down-left
            Square8x8.of(-1, 1),  // Up-left
            Square8x8.of(1, -1)   // Down-right
    );

    public static final List<Square8x8> ALL_DIRECTIONS = List.of(
            Square8x8.of(1, 1), Square8x8.of(-1, -1),
            Square8x8.of(-1, 1), Square8x8.of(1, -1),

            Square8x8.of(1, 0), Square8x8.of(0, 1),
            Square8x8.of(-1, 0), Square8x8.of(0, -1)
    );

    private final List<Square8x8> directions;

    public SlidingPieceMoveRule(P type, List<Square8x8> directions) {
        super(type);
        this.directions = directions;
    }

    @Override
    protected void generatePieceMoves(GameState<Square8x8, P> gameState,
                                      Square8x8 position,
                                      MoveLookup<Square8x8, P> moves) {
        ChessBoard<Square8x8, P> board = gameState.getChessBoard();
        ChessPiece<P> piece = gameState.getPieceAt(position);

        for (Square8x8 direction : directions) {
            Square8x8 current = position.offset(direction);

            while (board.getBounds().contains(current)) {
                ChessPiece<P> target = board.getPieceAt(current);

                if (target != null) {
                    // If target is an opponent's piece, add a capturing move
                    moves.add(Move.of(piece, position, current, new CaptureComponent<>(target)));
                    break;
                }

                // Add normal move if the square is empty
                moves.add(Move.of(piece, position, current));
                current = current.offset(direction);
            }
        }
    }
}
