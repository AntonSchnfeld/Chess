package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;
import de.schoenfeld.chess.rules.generative.GenerativeMoveRule;

import java.util.List;

/**
 * Abstract base class for generating moves of sliding chess pieces.
 * <p>
 * This rule applies to pieces that move in straight paths across the board, stopping at obstacles:
 * <ul>
 *     <li><strong>Rook</strong> - Moves in straight lines (horizontal/vertical).</li>
 *     <li><strong>Bishop</strong> - Moves in diagonal lines.</li>
 *     <li><strong>Queen</strong> - Moves in both straight and diagonal lines.</li>
 * </ul>
 * </p>
 *
 * <h3>Move Generation Process:</h3>
 * <ol>
 *     <li>Start from the current position of the piece.</li>
 *     <li>Iterate in all allowed movement directions.</li>
 *     <li>Move step by step in each direction until an obstacle is reached.</li>
 *     <li>If an enemy piece is found, generate a capturing move and stop further movement.</li>
 *     <li>If an empty square is found, generate a normal move and continue sliding.</li>
 * </ol>
 *
 * <p>
 * This class is meant to be extended by specific piece rules, such as {@link QueenMoveRule}, which will
 * provide the appropriate movement directions.
 * </p>
 *
 * @author Anton Schoenfeld
 */
public abstract class SlidingPieceMoveRule implements GenerativeMoveRule {

    /**
     * Movement directions for straight-line sliding pieces (Rook).
     */
    public static final List<Position> STRAIGHT_DIRECTIONS = List.of(
            Position.of(1, 0),  // Right
            Position.of(0, 1),  // Up
            Position.of(-1, 0), // Left
            Position.of(0, -1)  // Down
    );

    /**
     * Movement directions for diagonal sliding pieces (Bishop).
     */
    public static final List<Position> DIAGONAL_DIRECTIONS = List.of(
            Position.of(1, 1),   // Up-right
            Position.of(-1, -1), // Down-left
            Position.of(-1, 1),  // Up-left
            Position.of(1, -1)   // Down-right
    );

    /**
     * Movement directions for fully mobile sliding pieces (Queen).
     */
    public static final List<Position> ALL_DIRECTIONS = List.of(
            Position.of(1, 1), Position.of(-1, -1),
            Position.of(-1, 1), Position.of(1, -1),

            Position.of(1, 0), Position.of(0, 1),
            Position.of(-1, 0), Position.of(0, -1)
    );

    /**
     * The movement directions that this sliding piece can follow.
     */
    private final List<Position> directions;

    /**
     * Constructs a {@link SlidingPieceMoveRule} with the given movement directions.
     *
     * @param directions The allowed movement directions for the piece.
     */
    public SlidingPieceMoveRule(List<Position> directions) {
        this.directions = directions;
    }

    /**
     * Generates all legal moves for a given sliding piece and adds them to the provided {@link MoveCollection}.
     * <p>
     * The method iterates in all possible directions, stopping when it encounters a piece or the board boundary.
     * If an enemy piece is found, a capturing move is added. Empty squares are valid normal moves.
     * </p>
     *
     * @param gameState The current state of the chess game.
     * @param piece     The piece for which moves should be generated.
     * @param moves     The collection to which valid moves will be added.
     */
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
