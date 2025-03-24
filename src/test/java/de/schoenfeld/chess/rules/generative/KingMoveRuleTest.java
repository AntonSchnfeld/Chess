package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.model.*;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;
import de.schoenfeld.chess.move.components.MoveComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KingMoveRuleTest {
    private KingMoveRule tested;
    private GameState<StandardPieceType> gameState;
    private Square kingSquare;
    private ChessPiece<StandardPieceType> king;
    private ChessBoardBounds bounds;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        tested = new KingMoveRule();
        kingSquare = Square.of(3, 3);
        king = new ChessPiece<>(StandardPieceType.KING, true);
        bounds = new ChessBoardBounds(8, 8);
        gameState = mock(GameState.class);

        when(gameState.getPieceAt(kingSquare)).thenReturn(king);
        when(gameState.getSquaresWithTypeAndColour(StandardPieceType.KING, true))
                .thenReturn(List.of(kingSquare));
        when(gameState.getSquaresWithType(StandardPieceType.KING)).thenReturn(List.of(kingSquare));
        when(gameState.getBounds()).thenReturn(bounds);
        when(gameState.isWhiteTurn()).thenReturn(true);
    }

    @Test
    public void givenOnlyKingOnBoard_whenGenerateMoves_thenCanMoveInAllDirections() {
        // Given
        // GameState is already empty
        // When
        MoveCollection<StandardPieceType> moves = tested.generateMoves(gameState);
        // Then
        List<Square> expectedPositions = List.of(
                Square.of(2, 4), Square.of(3, 4), Square.of(4, 4),
                Square.of(2, 3)                      , Square.of(4, 3),
                Square.of(2, 2), Square.of(3, 2), Square.of(4, 2)
        );

        assertEquals(expectedPositions.size(), moves.size());
        for (Square square : expectedPositions)
            assertTrue(moves.containsMoveTo(square));
    }

    @Test
    public void givenTinyBoard_whenGenerateMoves_thenCantMove() {
        // Given
        bounds = new ChessBoardBounds(1, 1);
        kingSquare = Square.of(0, 0);

        when(gameState.getBounds()).thenReturn(bounds);
        when(gameState.getPieceAt(kingSquare)).thenReturn(king);
        when(gameState.getSquaresWithTypeAndColour(StandardPieceType.KING, true))
                .thenReturn(List.of(kingSquare));
        // When
        MoveCollection<StandardPieceType> moves = tested.generateMoves(gameState);

        // Then
        assertTrue(moves.isEmpty());
    }

    @Test
    public void givenPieceOnNeighbouringSquare_whenGenerateMoves_thenCaptures() {
        // Given
        ChessPiece<StandardPieceType> neighbour = new ChessPiece<>(StandardPieceType.PAWN, false);
        Square neighbourSquare = Square.of(4, 3);

        when(gameState.getPieceAt(neighbourSquare)).thenReturn(neighbour);
        // When
        MoveCollection<StandardPieceType> moves = tested.generateMoves(gameState);

        // Then
        assertTrue(moves.containsMoveTo(neighbourSquare));
        List<Move<StandardPieceType>> movesToNeighbourSquare = moves.getMovesTo(neighbourSquare);
        assertEquals(1, movesToNeighbourSquare.size());
        Move<StandardPieceType> capture = movesToNeighbourSquare.getFirst();
        assertTrue(capture.hasComponent(CaptureComponent.class));
        assertEquals(neighbour, capture.getComponent(CaptureComponent.class).capturedPiece());
    }
}