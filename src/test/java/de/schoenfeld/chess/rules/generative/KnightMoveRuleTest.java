package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.model.*;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KnightMoveRuleTest {
    private KnightMoveRule tested;
    private GameState<StandardPieceType> gameState;
    private ChessBoardBounds bounds;
    private ChessPiece<StandardPieceType> knight;
    private Square knightSquare;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        tested = new KnightMoveRule();
        bounds = new ChessBoardBounds(8, 8);
        knight = new ChessPiece<>(StandardPieceType.KNIGHT, true);
        knightSquare = Square.of(3, 3);
        gameState = mock(GameState.class);

        when(gameState.getPieceAt(knightSquare)).thenReturn(knight);
        when(gameState.getSquaresWithTypeAndColour(StandardPieceType.KNIGHT, true))
                .thenReturn(List.of(knightSquare));
        when(gameState.getSquaresWithType(StandardPieceType.KNIGHT))
                .thenReturn(List.of(knightSquare));
        when(gameState.getBounds()).thenReturn(bounds);
        when(gameState.isWhiteTurn()).thenReturn(true);
    }

    @Test
    public void givenOnlyKnightOnBoard_whenGenerateMoves_thenCanMoveInAllDirections() {
        // Given
        // Already setup
        // When
        MoveCollection<StandardPieceType> moves = tested.generateMoves(gameState);
        // Then
        List<Square> expectedPositions = List.of(
                Square.of(5, 4), Square.of(5, 2),
                Square.of(2, 5), Square.of(4, 5),
                Square.of(1, 4), Square.of(1, 2),
                Square.of(4, 1), Square.of(2, 1)
        );

        assertEquals(expectedPositions.size(), moves.size());
        for (Square square : expectedPositions)
            assertTrue(moves.containsMoveTo(square));
    }

    @Test
    public void givenTinyBoard_whenGenerateMoves_thenCantMove() {
        // Given
        bounds = new ChessBoardBounds(1, 1);
        knightSquare = Square.of(0, 0);

        when(gameState.getBounds()).thenReturn(bounds);
        when(gameState.getPieceAt(knightSquare)).thenReturn(knight);
        when(gameState.getSquaresWithTypeAndColour(StandardPieceType.KNIGHT, true))
                .thenReturn(List.of(knightSquare));
        // When
        MoveCollection<StandardPieceType> moves = tested.generateMoves(gameState);

        // Then
        assertTrue(moves.isEmpty());
    }

    @Test
    public void givenPieceOnCaptureSquare_whenGenerateMoves_thenCaptures() {
        // Given
        ChessPiece<StandardPieceType> captureCandidate =
                new ChessPiece<>(StandardPieceType.PAWN, false);
        Square captureCandidateSquare = Square.of(5, 4);

        when(gameState.getPieceAt(captureCandidateSquare)).thenReturn(captureCandidate);
        // When
        MoveCollection<StandardPieceType> moves = tested.generateMoves(gameState);

        // Then
        assertTrue(moves.containsMoveTo(captureCandidateSquare));
        List<Move<StandardPieceType>> movesToNeighbourSquare =
                moves.getMovesTo(captureCandidateSquare);
        assertEquals(1, movesToNeighbourSquare.size());
        Move<StandardPieceType> capture = movesToNeighbourSquare.getFirst();
        assertTrue(capture.hasComponent(CaptureComponent.class));
        assertEquals(captureCandidate, capture.getComponent(CaptureComponent.class).capturedPiece());
    }
}
