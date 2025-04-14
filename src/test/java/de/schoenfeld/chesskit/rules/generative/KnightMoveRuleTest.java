package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.board.ChessBoardBounds;
import de.schoenfeld.chesskit.board.Square8x8ChessBoardBounds;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.*;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.CaptureComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KnightMoveRuleTest {
    private KnightMoveRule<StandardPieceType> tested;
    private GameState<Square8x8, StandardPieceType> gameState;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        tested = KnightMoveRule.standard();
        ChessBoardBounds<Square8x8> bounds = new Square8x8ChessBoardBounds();
        ChessPiece<StandardPieceType> knight = new ChessPiece<>(StandardPieceType.KNIGHT, Color.WHITE);
        Square8x8 knightSquare8x8 = Square8x8.of(3, 3);
        gameState = mock(GameState.class);

        when(gameState.getPieceAt(knightSquare8x8)).thenReturn(knight);
        when(gameState.getTilesWithTypeAndColour(StandardPieceType.KNIGHT, Color.WHITE))
                .thenReturn(List.of(knightSquare8x8));
        when(gameState.getTilesWithType(StandardPieceType.KNIGHT))
                .thenReturn(List.of(knightSquare8x8));
        when(gameState.getBounds()).thenReturn(bounds);
        when(gameState.getColor()).thenReturn(Color.WHITE);
    }

    @Test
    public void givenOnlyKnightOnBoard_whenGenerateMoves_thenCanMoveInAllDirections() {
        // Given
        // Already setup
        // When
        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        tested.generateMoves(gameState, moves);
        // Then
        List<Square8x8> expectedPositions = List.of(
                Square8x8.of(5, 4), Square8x8.of(5, 2),
                Square8x8.of(2, 5), Square8x8.of(4, 5),
                Square8x8.of(1, 4), Square8x8.of(1, 2),
                Square8x8.of(4, 1), Square8x8.of(2, 1)
        );

        assertEquals(expectedPositions.size(), moves.size());
        for (Square8x8 square8x8 : expectedPositions)
            assertTrue(moves.containsMoveTo(square8x8));
    }

    @Test
    public void givenPieceOnCaptureSquare_whenGenerateMoves_thenCaptures() {
        // Given
        ChessPiece<StandardPieceType> captureCandidate =
                new ChessPiece<>(StandardPieceType.PAWN, Color.BLACK);
        Square8x8 captureCandidateSquare8x8 = Square8x8.of(5, 4);

        when(gameState.getPieceAt(captureCandidateSquare8x8)).thenReturn(captureCandidate);
        // When
        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        tested.generateMoves(gameState, moves);

        // Then
        assertTrue(moves.containsMoveTo(captureCandidateSquare8x8));
        List<Move<Square8x8, StandardPieceType>> movesToNeighbourSquare =
                moves.getMovesTo(captureCandidateSquare8x8);
        assertEquals(1, movesToNeighbourSquare.size());
        Move<Square8x8, StandardPieceType> capture = movesToNeighbourSquare.getFirst();
        assertTrue(capture.hasComponent(CaptureComponent.class));
        assertEquals(captureCandidate, capture.getComponent(CaptureComponent.class).capturedPiece());
    }
}
