package de.schoenfeld.chesskit.rules.restrictive;

import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.MoveGenerator;
import de.schoenfeld.chesskit.rules.SimpleMoveGenerator;
import de.schoenfeld.chesskit.rules.generative.*;
import de.schoenfeld.chesskit.rules.generative.sliding.BishopMoveRule;
import de.schoenfeld.chesskit.rules.generative.sliding.QueenMoveRule;
import de.schoenfeld.chesskit.rules.generative.sliding.RookMoveRule;

import java.util.Iterator;
import java.util.List;

public class CheckRule<T extends Tile, P extends PieceType> implements RestrictiveMoveRule<T, P> {
    private static final CheckRule<Square8x8, StandardPieceType> STANDARD = new CheckRule<>(
            new SimpleMoveGenerator<>(List.of(
                    PawnMoveRule.standard(),
                    KnightMoveRule.standard(),
                    BishopMoveRule.standard(),
                    RookMoveRule.standard(),
                    QueenMoveRule.standard(),
                    KingMoveRule.standard(),
                    CastlingRule.standard(),
                    EnPassantRule.standard()
            )),
            StandardPieceType.KING
    );

    public static CheckRule<Square8x8, StandardPieceType> standard() {
        return STANDARD;
    }

    private final MoveGenerator<T, P> moveGenerator;
    private final P kingType;

    public CheckRule(MoveGenerator<T, P> moveGenerator, P kingType) {
        this.moveGenerator = moveGenerator;
        this.kingType = kingType;
    }

    @Override
    public void filterMoves(MoveLookup<T, P> moves,
                            GameState<T, P> gameState) {
        // Approach to calculating check:
        // 1. Loop through all moves
        // 2. Simulate each move
        // 3. Get the king positions of the current player in the simulated future
        // 4. Continue if no king positions are found
        // 5. Generate the opponent moves
        // 6. Check if any opponent move targets any king

        Color color = gameState.getColor();
        Iterator<Move<T, P>> iterator = moves.iterator();
        while (iterator.hasNext()) {
            Move<T, P> move = iterator.next();

            // Simulate move, gameState now has opponent as turn
            gameState.makeMove(move);

            // Get the current player's king in the simulated future state
            List<T> kingSquare8x8s = gameState
                    .getTilesWithTypeAndColour(kingType, color);

            if (kingSquare8x8s.isEmpty()) {
                gameState.unmakeLastMove();
                continue;
            }

            MoveLookup<T, P> opponentMoves = moveGenerator.generatePseudoLegalMoves(gameState);

            for (T tile : kingSquare8x8s) {
                // If any opponent move targets the king's position, the move is illegal
                if (opponentMoves.containsMoveTo(tile)) {
                    iterator.remove();
                    break;
                }
            }

            gameState.unmakeLastMove();
        }
    }
}
