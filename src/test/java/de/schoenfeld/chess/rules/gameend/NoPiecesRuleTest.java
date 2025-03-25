package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NoPiecesRuleTest {
    private NoPiecesRule<StandardPieceType> tested;
    private GameState<StandardPieceType> gameState;
    private ChessBoard<StandardPieceType> chessBoard;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        tested = new NoPiecesRule<>();
        gameState = mock(GameState.class);
        chessBoard = mock(ChessBoard.class);

        when(gameState.getChessBoard()).thenReturn(chessBoard);
        when(gameState.isWhiteTurn()).thenReturn(true);

        when(chessBoard.getSquaresWithColour(true)).thenReturn(List.of());
        when(chessBoard.getSquaresWithColour(false)).thenReturn(List.of());
    }

    @Test
    public void givenBlackTurnAndBlackHasPieces_whenDetectGameEndCause_thenEmptyOptional() {
        // Given
        // Black turn, black has pieces
        when(gameState.isWhiteTurn()).thenReturn(false);
        when(chessBoard.getSquaresWithColour(false)).thenReturn(List.of(Square.a1));
        // When
        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);
        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void givenWhiteTurnAndWhiteHasPieces_whenDetectGameEndCause_thenEmptyOptional() {
        // Given
        // White turn, white has pieces
        when(gameState.isWhiteTurn()).thenReturn(true);
        when(chessBoard.getSquaresWithColour(true)).thenReturn(List.of(Square.a1));
        // When
        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);
        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void givenBlackTurnAndBlackHasNoPieces_whenDetectGameEndCause_thenGameConclusion() {
        // Given
        // White turn, black has no pieces
        when(gameState.isWhiteTurn()).thenReturn(false);
        when(chessBoard.getSquaresWithColour(false)).thenReturn(List.of());
        // When
        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);
        // Then
        assertTrue(result.isPresent());
        assertEquals(GameConclusion.Winner.WHITE, result.get().winner());
    }

    @Test
    public void givenWhiteTurnAndWhiteHasNoPieces_whenDetectGameEndCause_thenGameConclusion() {
        // Given
        // Black turn, white has no pieces
        when(gameState.isWhiteTurn()).thenReturn(true);
        when(chessBoard.getSquaresWithColour(true)).thenReturn(List.of());
        // When
        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);
        // Then
        assertTrue(result.isPresent());
        assertEquals(GameConclusion.Winner.BLACK, result.get().winner());
    }
}
