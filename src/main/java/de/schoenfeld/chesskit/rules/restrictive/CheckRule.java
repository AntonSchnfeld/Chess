package de.schoenfeld.chesskit.rules.restrictive;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.Square;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveCollection;
import de.schoenfeld.chesskit.rules.MoveGenerator;

import java.util.Iterator;
import java.util.List;

public class CheckRule implements RestrictiveMoveRule<StandardPieceType> {
    private final MoveGenerator<StandardPieceType> moveGenerator;

    public CheckRule(MoveGenerator<StandardPieceType> moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    @Override
    public void filterMoves(MoveCollection<StandardPieceType> moves,
                            GameState<StandardPieceType> gameState) {
        // Approach to calculating check:
        // 1. Loop through all moves
        // 2. Simulate each move
        // 3. Get the king positions of the current player in the simulated future
        // 4. Continue if no king positions are found
        // 5. Generate the opponent moves
        // 6. Check if any opponent move targets any king

        Iterator<Move<StandardPieceType>> iterator = moves.iterator();
        boolean isWhiteTurn = gameState.isWhiteTurn();
        while (iterator.hasNext()) {
            Move<StandardPieceType> move = iterator.next();

            // Simulate move, gameState now has opponent as turn
            move.executeOn(gameState);

            // Get the current player's king in the simulated future state
            List<Square> kingSquares = gameState
                    .getSquaresWithTypeAndColour(StandardPieceType.KING, isWhiteTurn);

            if (kingSquares.isEmpty()) {
                // This should never happen in a normal game, but we guard against it
                move.undoOn(gameState);
                continue;
            }

            MoveCollection<StandardPieceType> opponentMoves = moveGenerator.generateMoves(gameState);

            for (Square square : kingSquares) {
                // If any opponent move targets the king's position, the move is illegal
                if (opponentMoves.containsMoveTo(square)) {
                    iterator.remove();
                }
            }

            move.undoOn(gameState);
        }
    }
}
