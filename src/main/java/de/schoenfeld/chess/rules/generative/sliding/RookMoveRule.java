package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.MoveCollection;

/**
 * Move rule for rooks, generating all legal moves based on sliding movement in straight directions.
 * <p>
 * The rook moves horizontally or vertically in straight lines, either capturing opposing pieces
 * or moving to empty squares along its path. It cannot move diagonally.
 * </p>
 *
 * <h3>Implementation Details:</h3>
 * <ul>
 *     <li>Uses {@link SlidingPieceMoveRule} to handle straight-line movement logic.</li>
 *     <li>Retrieves all rooks of the active player from the board.</li>
 *     <li>Calls {@code generateMoves()} from {@link SlidingPieceMoveRule} for each rook.</li>
 * </ul>
 *
 * @author Anton Schoenfeld
 */
public class RookMoveRule extends SlidingPieceMoveRule {

    /**
     * Constructs a new {@code RookMoveRule} using straight directions only.
     */
    public RookMoveRule() {
        super(SlidingPieceMoveRule.STRAIGHT_DIRECTIONS);
    }

    /**
     * Generates all legal moves for rooks of the current player.
     *
     * @param gameState The current state of the game.
     * @return A {@code MoveCollection} containing all possible rook moves.
     */
    @Override
    public MoveCollection generateMoves(GameState gameState) {
        var board = gameState.chessBoard();
        var rooks = board.getPiecesOfType(PieceType.ROOK, gameState.isWhiteTurn());
        var moves = new MoveCollection();

        for (var rook : rooks) {
            generateMoves(gameState, rook, moves);
        }

        return moves;
    }
}
