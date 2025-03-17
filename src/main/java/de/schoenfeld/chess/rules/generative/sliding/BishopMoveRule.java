package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.MoveCollection;

/**
 * Move rule for bishops, generating all legal moves based on sliding movement in diagonal directions.
 * <p>
 * The bishop moves diagonally in all four diagonal directions. It can move as far as there are no obstacles in its path,
 * capturing any opposing pieces along the way.
 * </p>
 *
 * <h3>Implementation Details:</h3>
 * <ul>
 *     <li>Uses {@link SlidingPieceMoveRule} to handle diagonal movement logic.</li>
 *     <li>Retrieves all bishops of the active player from the board.</li>
 *     <li>Calls {@code generateMoves()} from {@link SlidingPieceMoveRule} for each bishop.</li>
 * </ul>
 *
 * @author Anton Schoenfeld
 */
public class BishopMoveRule extends SlidingPieceMoveRule {

    /**
     * Constructs a new {@code BishopMoveRule} using diagonal directions only.
     */
    public BishopMoveRule() {
        super(SlidingPieceMoveRule.DIAGONAL_DIRECTIONS);
    }

    /**
     * Generates all legal moves for bishops of the current player.
     *
     * @param gameState The current state of the game.
     * @return A {@code MoveCollection} containing all possible bishop moves.
     */
    @Override
    public MoveCollection generateMoves(GameState gameState) {
        var board = gameState.chessBoard();
        var bishops = board.getPiecesOfType(PieceType.BISHOP, gameState.isWhiteTurn());
        var moves = new MoveCollection();

        for (var bishop : bishops) {
            generateMoves(gameState, bishop, moves);
        }

        return moves;
    }
}
