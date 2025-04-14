package de.schoenfeld.chesskit.rules.gameend;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.events.GameConclusion;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.StandardPieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NoPiecesDetectorTest {
    private NoPiecesDetector<Square8x8, StandardPieceType> tested;
    private GameState<Square8x8, StandardPieceType> gameState;
    private ChessBoard<Square8x8, StandardPieceType> chessBoard;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        tested = new NoPiecesDetector<>();
        gameState = mock(GameState.class);
        chessBoard = mock(ChessBoard.class);

        when(gameState.getChessBoard()).thenReturn(chessBoard);
        when(gameState.getColor()).thenReturn(Color.WHITE);

        when(chessBoard.getTilesWithColour(Color.WHITE)).thenReturn(List.of());
        when(chessBoard.getTilesWithColour(Color.BLACK)).thenReturn(List.of());
    }

    @Test
    public void givenBlackTurnAndBlackHasPieces_whenDetectGameEndCause_thenEmptyOptional() {
        // Given
        // Black turn, black has pieces
        when(gameState.getColor()).thenReturn(Color.BLACK);
        when(chessBoard.getTilesWithColour(Color.BLACK)).thenReturn(List.of(Square8x8.of(0, 0)));
        // When
        GameConclusion result = tested.detectConclusion(gameState);
        // Then
        assertNull(result);
    }

    @Test
    public void givenWhiteTurnAndWhiteHasPieces_whenDetectGameEndCause_thenEmptyOptional() {
        // Given
        // White turn, white has pieces
        when(gameState.getColor()).thenReturn(Color.WHITE);
        when(chessBoard.getTilesWithColour(Color.WHITE)).thenReturn(List.of(Square8x8.of(0, 0)));
        // When
        GameConclusion result = tested.detectConclusion(gameState);
        // Then
        assertNull(result);
    }

    @Test
    public void givenBlackTurnAndBlackHasNoPieces_whenDetectGameEndCause_thenGameConclusion() {
        // Given
        // White turn, black has no pieces
        when(gameState.getColor()).thenReturn(Color.BLACK);
        when(chessBoard.getTilesWithColour(Color.BLACK)).thenReturn(List.of());
        // When
        GameConclusion result = tested.detectConclusion(gameState);
        // Then
        assertNotNull(result);
        assertEquals(GameConclusion.Winner.WHITE, result.winner());
    }

    @Test
    public void givenWhiteTurnAndWhiteHasNoPieces_whenDetectGameEndCause_thenGameConclusion() {
        // Given
        // Black turn, white has no pieces
        when(gameState.getColor()).thenReturn(Color.WHITE);
        when(chessBoard.getTilesWithColour(Color.WHITE)).thenReturn(List.of());
        // When
        GameConclusion result = tested.detectConclusion(gameState);
        // Then
        assertNotNull(result);
        assertEquals(GameConclusion.Winner.BLACK, result.winner());
    }
}
