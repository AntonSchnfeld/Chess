package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.board.MapChessBoard;
import de.schoenfeld.chesskit.board.Square8x8ChessBoardBounds;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.board.tile.Square8x8;
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

    private PawnMoveRule<StandardPieceType> tested;
    private GameState<Square8x8, StandardPieceType> gameState;

    @BeforeEach
    public void setup() {
        tested = PawnMoveRule.standard();
        gameState = new GameState<>(new MapChessBoard<>(new Square8x8ChessBoardBounds()), Rules.standard()); // White's turn
    }

    @Test
    public void givenWhitePawnNotOnPromotionRank_whenGenerateMoves_thenNormalOneAndTwoStepMoves() {
        // Pawn position: (4,1)
        Square8x8 pawnSquare8x8 = Square8x8.of(4, 1);

        // Create a white pawn that hasn't moved
        ChessPiece<StandardPieceType> pawn = new ChessPiece<>(StandardPieceType.PAWN, Color.WHITE);

        // Add to game state
        gameState.setPieceAt(pawnSquare8x8, pawn);

        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        tested.generateMoves(gameState, moves);

        // Expected: one-step to (4,2) and two-step to (4,3)
        assertEquals(2, moves.size(), "Expected two non-promotion moves for a pawn at (4,1)");

        Iterator<Move<Square8x8, StandardPieceType>> iterator = moves.iterator();
        Move<Square8x8, StandardPieceType> move1 = iterator.next();
        Move<Square8x8, StandardPieceType> move2 = iterator.next();

        List<Square8x8> targets = List.of(move1.to(), move2.to());
        assertTrue(targets.contains(Square8x8.of(4, 2)));
        assertTrue(targets.contains(Square8x8.of(4, 3)));
    }

    @Test
    public void givenWhitePawnOnPromotionRow_whenGenerateMoves_thenPromotionMovesGenerated() {
        // Pawn position: (4,6)
        Square8x8 pawnSquare8x8 = Square8x8.of(4, 6);

        // Create a white pawn that has moved already (so no two-step move).
        ChessPiece<StandardPieceType> pawn = new ChessPiece<>(StandardPieceType.PAWN, Color.WHITE);

        // Add to game state
        gameState.setPieceAt(pawnSquare8x8, pawn);

        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        tested.generateMoves(gameState, moves);

        // Expected: one move that's a promotion with 4 promotion options.
        assertEquals(4, moves.size(), "Expected four promotion moves for a pawn reaching promotion rank");

        // Check that all moves lead to promotion target (4,7)
        for (Move<Square8x8, StandardPieceType> move : moves) {
            assertEquals(Square8x8.of(4, 7), move.to(), "Promotion moves should land at (4,7)");
            // You could also inspect the promotion component if needed.
        }
    }

    @Test
    public void givenWhitePawnCaptureAvailable_whenGenerateMoves_thenCaptureMoveGenerated() {
        Square8x8 pawnSquare8x8 = Square8x8.of(4, 1);

        // Create a white pawn at (4,1)
        ChessPiece<StandardPieceType> pawn = new ChessPiece<>(StandardPieceType.PAWN, Color.WHITE);
        gameState.setPieceAt(pawnSquare8x8, pawn);

        // Add to game state
        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        tested.generateMoves(gameState, moves);

        // Place an enemy pawn at left capture square.
        // For white pawn, direction = 1; left capture: offset(-1, 1)
        Square8x8 captureSquare8x8 = pawnSquare8x8.offset(-1, 1); // (3,2)
        ChessPiece<StandardPieceType> enemyPawn = new ChessPiece<>(StandardPieceType.PAWN, Color.BLACK);

        // Add to game state
        gameState.setPieceAt(captureSquare8x8, enemyPawn);

        moves.clear();
        tested.generateMoves(gameState, moves);

        // Expect at least one capture move (non-promotion since target rank is 2, not 7)
        boolean captureFound = moves.stream().anyMatch(m -> m.to().equals(captureSquare8x8));
        assertTrue(captureFound, "Expected a capture move to " + captureSquare8x8);
    }

    @Test
    public void givenWhitePawnCapturePromotion_whenGenerateMoves_thenPromotionCaptureMovesGenerated() {
        Square8x8 pawnSquare8x8 = Square8x8.of(4, 6);

        // Create a white pawn at (4,6)
        ChessPiece<StandardPieceType> pawn = new ChessPiece<>(StandardPieceType.PAWN, Color.WHITE);

        // Add to game state
        gameState.setPieceAt(pawnSquare8x8, pawn);

        // For a white pawn, one-step move from (4,6) goes to (4,7) - promotion rank.
        // Let's test capture from the left: left capture is pawnSquare.offset(-1,1) = (3,7)
        Square8x8 captureSquare8x8 = pawnSquare8x8.offset(-1, 1); // (3,7)
        ChessPiece<StandardPieceType> enemyPawn = new ChessPiece<>(StandardPieceType.PAWN, Color.BLACK);

        // Add to game state
        gameState.setPieceAt(captureSquare8x8, enemyPawn);

        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        tested.generateMoves(gameState, moves);

        // Expect promotion capture moves: one per promotion type (4 moves) that capture on (3,7)
        long promoCaptureCount = moves.stream().filter(m -> m.to().equals(captureSquare8x8)).count();
        assertEquals(4, promoCaptureCount, "Expected 4 promotion capture moves from " + pawnSquare8x8 + " to " + captureSquare8x8);
    }
}
