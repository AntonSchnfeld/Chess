package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.*;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PawnMoveRuleTest {

    private PawnMoveRule pawnMoveRule;
    private GameState<StandardPieceType> gameState;
    private ChessBoard<StandardPieceType> board;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        pawnMoveRule = new PawnMoveRule();
        gameState = mock(GameState.class);
        board = mock(ChessBoard.class);
        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);

        when(gameState.chessBoard()).thenReturn(board);
        when(gameState.getBounds()).thenReturn(bounds);
        // Assume white's turn for these tests.
        when(gameState.isWhiteTurn()).thenReturn(true);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenWhitePawnNotOnPromotionRank_whenGenerateMoves_thenNormalOneAndTwoStepMoves() {
        // Pawn position: (4,1)
        Square pawnSquare = Square.of(4, 1);
        // Set up board: pawn at (4,1)
        when(board.getSquaresWithTypeAndColour(StandardPieceType.PAWN, true))
                .thenReturn(List.of(pawnSquare));
        // For any square not occupied, gameState.getPieceAt returns null.
        when(gameState.getPieceAt(any(Square.class))).thenReturn(null);

        // Create a white pawn that has not moved.
        ChessPiece<StandardPieceType> pawn = mock(ChessPiece.class);
        when(pawn.pieceType()).thenReturn(StandardPieceType.PAWN);
        when(pawn.isWhite()).thenReturn(true);
        when(pawn.hasMoved()).thenReturn(false);

        // When: The pawn is retrieved from the game state.
        // For our test, simulate that getPieceAt returns the pawn at pawnSquare.
        when(gameState.getPieceAt(pawnSquare)).thenReturn(pawn);

        MoveCollection<StandardPieceType> moves = pawnMoveRule.generateMoves(gameState);

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
    @SuppressWarnings("unchecked")
    public void givenWhitePawnOnPromotionRow_whenGenerateMoves_thenPromotionMovesGenerated() {
        // Pawn position: (4,6)
        Square pawnSquare = Square.of(4, 6);
        when(board.getSquaresWithTypeAndColour(StandardPieceType.PAWN, true))
                .thenReturn(List.of(pawnSquare));
        when(gameState.getPieceAt(any(Square.class))).thenReturn(null);

        // Create a white pawn that has moved already (so no two-step move).
        ChessPiece<StandardPieceType> pawn = mock(ChessPiece.class);
        when(pawn.pieceType()).thenReturn(StandardPieceType.PAWN);
        when(pawn.isWhite()).thenReturn(true);
        when(pawn.hasMoved()).thenReturn(true);
        when(gameState.getPieceAt(pawnSquare)).thenReturn(pawn);

        MoveCollection<StandardPieceType> moves = pawnMoveRule.generateMoves(gameState);

        // Expected: one move that is a promotion with 4 promotion options.
        // Since addPromotionMoves adds one move per promotion type.
        // Default promotion types: QUEEN, ROOK, BISHOP, KNIGHT => 4 moves.
        assertEquals(4, moves.size(), "Expected four promotion moves for a pawn reaching promotion rank");

        // Check that all moves lead to promotion target (4,7)
        for (Move<StandardPieceType> move : moves) {
            assertEquals(Square.of(4, 7), move.to(), "Promotion moves should land at (4,7)");
            // You could also inspect the promotion component if needed.
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenWhitePawnCaptureAvailable_whenGenerateMoves_thenCaptureMoveGenerated() {
        Square pawnSquare = Square.of(4, 1);
        when(board.getSquaresWithTypeAndColour(StandardPieceType.PAWN, true))
                .thenReturn(List.of(pawnSquare));
        // Default: empty squares except for capture square.
        when(gameState.getPieceAt(any(Square.class))).thenReturn(null);

        // Create a white pawn at (4,1)
        ChessPiece<StandardPieceType> pawn = mock(ChessPiece.class);
        when(pawn.pieceType()).thenReturn(StandardPieceType.PAWN);
        when(pawn.isWhite()).thenReturn(true);
        when(pawn.hasMoved()).thenReturn(true);
        when(gameState.getPieceAt(pawnSquare)).thenReturn(pawn);

        // Place an enemy pawn at left capture square.
        // For white pawn, direction = 1; left capture: offset(-1, 1)
        Square captureSquare = pawnSquare.offset(-1, 1); // (3,2)
        ChessPiece<StandardPieceType> enemyPawn = mock(ChessPiece.class);
        when(enemyPawn.pieceType()).thenReturn(StandardPieceType.PAWN);
        when(enemyPawn.isWhite()).thenReturn(false);
        when(gameState.getPieceAt(captureSquare)).thenReturn(enemyPawn);

        MoveCollection<StandardPieceType> moves = pawnMoveRule.generateMoves(gameState);

        // Expect at least one capture move (non-promotion since target rank is 2, not 7)
        boolean captureFound = moves.stream().anyMatch(m -> m.to().equals(captureSquare));
        assertTrue(captureFound, "Expected a capture move to " + captureSquare);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenWhitePawnCapturePromotion_whenGenerateMoves_thenPromotionCaptureMovesGenerated() {
        Square pawnSquare = Square.of(4, 6);
        when(board.getSquaresWithTypeAndColour(StandardPieceType.PAWN, true))
                .thenReturn(List.of(pawnSquare));
        when(gameState.getPieceAt(any(Square.class))).thenReturn(null);

        // Create a white pawn at (4,6)
        ChessPiece<StandardPieceType> pawn = mock(ChessPiece.class);
        when(pawn.pieceType()).thenReturn(StandardPieceType.PAWN);
        when(pawn.isWhite()).thenReturn(true);
        when(pawn.hasMoved()).thenReturn(true);
        when(gameState.getPieceAt(pawnSquare)).thenReturn(pawn);

        // For a white pawn, one-step move from (4,6) goes to (4,7) - promotion rank.
        // Let's test capture from left: left capture is pawnSquare.offset(-1,1) = (3,7)
        Square captureSquare = pawnSquare.offset(-1, 1); // (3,7)
        ChessPiece<StandardPieceType> enemyPawn = mock(ChessPiece.class);
        when(enemyPawn.pieceType()).thenReturn(StandardPieceType.PAWN);
        when(enemyPawn.isWhite()).thenReturn(false);
        when(gameState.getPieceAt(captureSquare)).thenReturn(enemyPawn);

        MoveCollection<StandardPieceType> moves = pawnMoveRule.generateMoves(gameState);

        // Expect promotion capture moves: one per promotion type (4 moves) that capture on (3,7)
        long promoCaptureCount = moves.stream().filter(m -> m.to().equals(captureSquare)).count();
        assertEquals(4, promoCaptureCount, "Expected 4 promotion capture moves from " + pawnSquare + " to " + captureSquare);
    }
}
