package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;

import java.util.List;

/**
 * A {@link GenerativeMoveRule} implementation that generates all possible moves for kings.
 * This rule does not handle castling. Castling is handled by {@link CastlingRule}.
 *
 * @author Anton Schoenfeld
 */
public class KingMoveRule implements GenerativeMoveRule {
    private static final List<Position> KING_DIRECTIONS = List.of(
            new Position(1, 0), new Position(-1, 0), new Position(0, 1), new Position(0, -1),
            new Position(1, 1), new Position(-1, -1), new Position(1, -1), new Position(-1, 1)
    );

    /**
     * Generates all possible moves for a given king.
     *
     * @param gameState The current game state
     * @param king      The king to generate moves for
     * @param moves     The collection to add the generated moves to
     */
    private static void generateKingMoves(GameState gameState,
                                          ChessPiece king,
                                          MoveCollection moves) {
        // Generate moves in all directions
        for (var direction : KING_DIRECTIONS) {
            var from = gameState.chessBoard().getPiecePosition(king);
            var to = from.offset(direction);
            // Check if the target position is on the board
            if (gameState.chessBoard().getBounds().contains(to)) {
                var targetPiece = gameState.chessBoard().getPieceAt(to);
                // Check if the target position is empty or contains an enemy piece
                if (targetPiece == null) moves.add(Move.of(king, from, to));
                    // Capture
                else if (targetPiece.isWhite() != king.isWhite())
                    moves.add(Move.of(king, from, to, new CaptureComponent(targetPiece)));
            }
        }
    }

    /**
     * Generates all possible moves for the kings in the current game state.
     * Does not generate castling moves.
     *
     * @param gameState The current game state
     * @return A {@link MoveCollection} containing the generated moves
     * @throws NullPointerException if {@code gameState} is null
     */
    @Override
    public MoveCollection generateMoves(GameState gameState) {
        var moves = new MoveCollection();
        var board = gameState.chessBoard();

        var kings = board.getPiecesOfType(PieceType.KING, gameState.isWhiteTurn())
                .stream()
                .toList();

        for (var king : kings) generateKingMoves(gameState, king, moves);

        return moves;
    }
}
