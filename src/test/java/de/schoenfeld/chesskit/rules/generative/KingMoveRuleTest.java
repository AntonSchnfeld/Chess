package de.schoenfeld.chesskit.rules.generative;

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

public class KingMoveRuleTest {
    private KingMoveRule<StandardPieceType> tested;
    private GameState<Square8x8, StandardPieceType> gameState;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        tested = KingMoveRule.standard();
        Square8x8 kingSquare8x8 = Square8x8.of(3, 3);
        ChessPiece<StandardPieceType> king = new ChessPiece<>(StandardPieceType.KING, Color.WHITE);
        Square8x8ChessBoardBounds bounds = new Square8x8ChessBoardBounds();
        gameState = mock(GameState.class);

        when(gameState.getPieceAt(kingSquare8x8)).thenReturn(king);
        when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, Color.WHITE))
                .thenReturn(List.of(kingSquare8x8));
        when(gameState.getTilesWithType(StandardPieceType.KING)).thenReturn(List.of(kingSquare8x8));
        when(gameState.getBounds()).thenReturn(bounds);
        when(gameState.getColor()).thenReturn(Color.WHITE);
    }

    @Test
    public void givenOnlyKingOnBoard_whenGenerateMoves_thenCanMoveInAllDirections() {
        // Given
        // GameState is already empty
        // When
        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        tested.generateMoves(gameState, moves);
        // Then
        List<Square8x8> expectedPositions = List.of(
                Square8x8.of(2, 4), Square8x8.of(3, 4), Square8x8.of(4, 4),
                Square8x8.of(2, 3), Square8x8.of(4, 3),
                Square8x8.of(2, 2), Square8x8.of(3, 2), Square8x8.of(4, 2)
        );

        assertEquals(expectedPositions.size(), moves.size());
        for (Square8x8 square8x8 : expectedPositions)
            assertTrue(moves.containsMoveTo(square8x8));
    }

    @Test
    public void givenPieceOnNeighbouringSquare_whenGenerateMoves_thenCaptures() {
        // Given
        ChessPiece<StandardPieceType> neighbour = new ChessPiece<>(StandardPieceType.PAWN, Color.BLACK);
        Square8x8 neighbourSquare8x8 = Square8x8.of(4, 3);

        when(gameState.getPieceAt(neighbourSquare8x8)).thenReturn(neighbour);
        // When
        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        tested.generateMoves(gameState, moves);

        // Then
        assertTrue(moves.containsMoveTo(neighbourSquare8x8));
        List<Move<Square8x8, StandardPieceType>> movesToNeighbourSquare = moves.getMovesTo(neighbourSquare8x8);
        assertEquals(1, movesToNeighbourSquare.size());
        Move<Square8x8, StandardPieceType> capture = movesToNeighbourSquare.getFirst();
        assertTrue(capture.hasComponent(CaptureComponent.class));
        assertEquals(neighbour, capture.getComponent(CaptureComponent.class).capturedPiece());
    }
}