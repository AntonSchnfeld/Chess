package de.schoenfeld.chess.rules.generative.sliding;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.MoveCollection;

/**
 * Move rule for queens, generating all legal moves based on sliding movement in all directions.
 * <p>
 * The queen moves like both a rook and a bishop, meaning it can slide:
 * <ul>
 *     <li>Horizontally (left/right)</li>
 *     <li>Vertically (up/down)</li>
 *     <li>Diagonally</li>
 * </ul>
 * </p>
 *
 * <h3>Implementation Details:</h3>
 * <ul>
 *     <li>Uses {@link SlidingPieceMoveRule} to handle movement logic.</li>
 *     <li>Retrieves all queens of the active player from the board.</li>
 *     <li>Calls {@code generateMoves()} from {@link SlidingPieceMoveRule} for each queen.</li>
 * </ul>
 *
 * @author Anton Schoenfeld
 */
public class QueenMoveRule extends SlidingPieceMoveRule {

    /**
     * Constructs a new {@link QueenMoveRule} using all possible sliding directions.
     */
    public QueenMoveRule() {
        super(SlidingPieceMoveRule.ALL_DIRECTIONS);
    }

    /**
     * Generates all legal moves for queens of the current player.
     *
     * @param gameState The current state of the game.
     * @return A {@link MoveCollection} containing all possible queen moves.
     */
    @Override
    public MoveCollection generateMoves(GameState gameState) {
        var queens = gameState.chessBoard().getPiecesOfType(PieceType.QUEEN, gameState.isWhiteTurn());
        var moves = new MoveCollection();

        for (var queen : queens) {
            generateMoves(gameState, queen, moves);
        }

        return moves;
    }
}
