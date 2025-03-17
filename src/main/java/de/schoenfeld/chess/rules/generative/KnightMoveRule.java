package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;

import java.util.List;

/**
 * A rule for generating all possible knight moves in a given game state.
 * <p>
 * The knight moves in an L-shape: two squares in one direction and one square perpendicular,
 * or vice versa. Knights are unique in that they can "jump" over other pieces, meaning
 * obstacles on the board do not affect their movement.
 * </p>
 * <p>
 * This class ensures that all legal knight moves are generated, including:
 * <ul>
 *   <li>Normal moves to empty squares.</li>
 *   <li>Capturing enemy pieces.</li>
 * </ul>
 * </p>
 *
 * <h3>Move Generation Process:</h3>
 * <ol>
 *   <li>Find all knight pieces belonging to the current player.</li>
 *   <li>For each knight, calculate its potential destinations using predefined offsets.</li>
 *   <li>Check that the destination square is within the board bounds.</li>
 *   <li>Ensure that the destination is either empty or occupied by an enemy piece.</li>
 *   <li>Add the valid move to the {@link MoveCollection}.</li>
 * </ol>
 *
 * <p><strong>Note:</strong> This rule does not check for check conditions;
 * that responsibility belongs to restrictive move rules.</p>
 *
 * @author Anton Schoenfeld
 */
public class KnightMoveRule implements GenerativeMoveRule {
    private static final List<Position> KNIGHT_MOVES = List.of(
            new Position(2, 1), new Position(2, -1), new Position(-2, 1), new Position(-2, -1),
            new Position(1, 2), new Position(1, -2), new Position(-1, 2), new Position(-1, -2)
    );

    /**
     * Generates all possible moves for a given knight.
     *
     * @param board  The current chess board
     * @param knight The knight to generate moves for
     * @param moves  The collection to add the generated moves to
     */
    private static void generateKnightMoves(ImmutableChessBoard board,
                                            ChessPiece knight,
                                            MoveCollection moves) {
        var from = board.getPiecePosition(knight);

        for (var offset : KNIGHT_MOVES) {
            var to = from.offset(offset.x(), offset.y());

            // Ensure the move stays within the board boundaries
            if (board.getBounds().contains(to)) {
                var targetPiece = board.getPieceAt(to);
                // Allow the move if the destination is empty or occupied by an opponent's piece
                if (targetPiece == null) moves.add(Move.of(knight, from, to));
                else if (targetPiece.isWhite() != knight.isWhite())
                    moves.add(Move.of(knight, from, to, new CaptureComponent(targetPiece)));
            }
        }
    }

    /**
     * Generates all legal moves for knights in the given {@link GameState}.
     * <p>
     * This method does not consider special rules such as check restrictions.
     * It simply finds all knights of the current player and generates their possible moves.
     * </p>
     *
     * @param gameState The current state of the chess game.
     * @return A {@link MoveCollection} containing all valid knight moves.
     */
    @Override
    public MoveCollection generateMoves(GameState gameState) {
        var moves = new MoveCollection();
        var board = gameState.chessBoard();

        // Retrieve all knights belonging to the current player
        var knights = board.getPiecesOfType(PieceType.KNIGHT, gameState.isWhiteTurn());

        for (var knight : knights) generateKnightMoves(board, knight, moves);

        return moves;
    }
}
