package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.Square;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.Rules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PawnMoveRuleTest {

    private PawnMoveRule<StandardPieceType> pawnMoveRule;
    private GameState<StandardPieceType> gameState;

    @BeforeEach
    public void setup() {
        pawnMoveRule = PawnMoveRule.standard();
        gameState = new GameState<>(Rules.standard()); // White's turn
    }

    @Test
    public void givenWhitePawnNotOnPromotionRank_whenGenerateMoves_thenNormalOneAndTwoStepMoves() {
        // Pawn position: (4,1)
        Square pawnSquare = Square.of(4, 1);

        // Create a white pawn that has not moved
        ChessPiece<StandardPieceType> pawn = new ChessPiece<>(StandardPieceType.PAWN, true, false);

        // Add to game state
        gameState.setPieceAt(pawnSquare, pawn);

        MoveLookup<StandardPieceType> moves = pawnMoveRule.generateMoves(gameState);

        // Expected: one-step to (4,2) and two-step to (4,3)
        assertEquals(2, moves.size(), "Expected two non-promotion moves for a pawn at (4,1)");

        Iterator<Move<StandardPieceType>> iterator = moves.iterator();
        Move<StandardPieceType> move1 = iterator.next();
        Move<StandardPieceType> move2 = iterator.next();

        List<Square> targets = List.of(move1.to(), move2.to());
        assertTrue(targets.contains(Square.of(4, 2)));
        assertTrue(targets.contains(Square.of(4, 3)));
    }

    @Test
    public void givenWhitePawnOnPromotionRow_whenGenerateMoves_thenPromotionMovesGenerated() {
        // Pawn position: (4,6)
        Square pawnSquare = Square.of(4, 6);

        // Create a white pawn that has moved already (so no two-step move).
        ChessPiece<StandardPieceType> pawn = new ChessPiece<>(StandardPieceType.PAWN, true, true);

        // Add to game state
        gameState.setPieceAt(pawnSquare, pawn);

        MoveLookup<StandardPieceType> moves = pawnMoveRule.generateMoves(gameState);

        // Expected: one move that is a promotion with 4 promotion options.
        assertEquals(4, moves.size(), "Expected four promotion moves for a pawn reaching promotion rank");

        // Check that all moves lead to promotion target (4,7)
        for (Move<StandardPieceType> move : moves) {
            assertEquals(Square.of(4, 7), move.to(), "Promotion moves should land at (4,7)");
            // You could also inspect the promotion component if needed.
        }
    }

    @Test
    public void givenWhitePawnCaptureAvailable_whenGenerateMoves_thenCaptureMoveGenerated() {
        Square pawnSquare = Square.of(4, 1);

        // Create a white pawn at (4,1)
        ChessPiece<StandardPieceType> pawn = new ChessPiece<>(StandardPieceType.PAWN, true, true);

        // Add to game state
        gameState.setPieceAt(pawnSquare, pawn);

        // Place an enemy pawn at left capture square.
        // For white pawn, direction = 1; left capture: offset(-1, 1)
        Square captureSquare = pawnSquare.offset(-1, 1); // (3,2)
        ChessPiece<StandardPieceType> enemyPawn = new ChessPiece<>(StandardPieceType.PAWN, false, false);

        // Add to game state
        gameState.setPieceAt(captureSquare, enemyPawn);

        MoveLookup<StandardPieceType> moves = pawnMoveRule.generateMoves(gameState);

        // Expect at least one capture move (non-promotion since target rank is 2, not 7)
        boolean captureFound = moves.stream().anyMatch(m -> m.to().equals(captureSquare));
        assertTrue(captureFound, "Expected a capture move to " + captureSquare);
    }

    @Test
    public void givenWhitePawnCapturePromotion_whenGenerateMoves_thenPromotionCaptureMovesGenerated() {
        Square pawnSquare = Square.of(4, 6);

        // Create a white pawn at (4,6)
        ChessPiece<StandardPieceType> pawn = new ChessPiece<>(StandardPieceType.PAWN, true, true);

        // Add to game state
        gameState.setPieceAt(pawnSquare, pawn);

        // For a white pawn, one-step move from (4,6) goes to (4,7) - promotion rank.
        // Let's test capture from left: left capture is pawnSquare.offset(-1,1) = (3,7)
        Square captureSquare = pawnSquare.offset(-1, 1); // (3,7)
        ChessPiece<StandardPieceType> enemyPawn = new ChessPiece<>(StandardPieceType.PAWN, false, false);

        // Add to game state
        gameState.setPieceAt(captureSquare, enemyPawn);

        MoveLookup<StandardPieceType> moves = pawnMoveRule.generateMoves(gameState);

        // Expect promotion capture moves: one per promotion type (4 moves) that capture on (3,7)
        long promoCaptureCount = moves.stream().filter(m -> m.to().equals(captureSquare)).count();
        assertEquals(4, promoCaptureCount, "Expected 4 promotion capture moves from " + pawnSquare + " to " + captureSquare);
    }
}
