package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InsufficientMaterialRuleTest {

    private void setupBoardMock(ChessBoard<StandardPieceType> board,
                                StandardPieceType... pieceTypes) {
        // Assume kings at default positions
        when(board.getSquaresWithType(StandardPieceType.KING))
                .thenReturn(List.of(Square.of(4, 0), Square.of(4, 7)));

        for (StandardPieceType pieceType : pieceTypes) {
            List<Square> squares = board.getSquaresWithType(pieceType);
            if (squares == null) squares = new ArrayList<>();
            squares.add(Square.a1);
            when(board.getSquaresWithType(pieceType)).thenReturn(squares);
        }
    }

    @SuppressWarnings("unchecked")
    private void testInsufficientMaterial(StandardPieceType... pieceTypes) {
        ChessBoard<StandardPieceType> board = mock(ChessBoard.class);
        GameState<StandardPieceType> gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);
        setupBoardMock(board, pieceTypes);

        InsufficientMaterialRule rule = new InsufficientMaterialRule();
        Optional<GameConclusion> conclusion = rule.detectGameEndCause(gameState);

        assertTrue(conclusion.isPresent());
        assertEquals("Insufficient material", conclusion.get().description());
        assertEquals(GameConclusion.Winner.NONE, conclusion.get().winner());
    }

    @SuppressWarnings("unchecked")
    private void testSufficientMaterial(StandardPieceType... pieceTypes) {
        ChessBoard<StandardPieceType> board = mock(ChessBoard.class);
        GameState<StandardPieceType> gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);
        setupBoardMock(board, pieceTypes);

        InsufficientMaterialRule rule = new InsufficientMaterialRule();
        Optional<GameConclusion> conclusion = rule.detectGameEndCause(gameState);

        assertFalse(conclusion.isPresent());
    }

    @Test
    public void givenKingVsKing_whenDetectGameEndCause_thenInsufficientMaterial() {
        testInsufficientMaterial();
    }

    @Test
    public void givenKingAndBishopVsKing_whenDetectGameEndCause_thenInsufficientMaterial() {
        testInsufficientMaterial(StandardPieceType.BISHOP);
    }

    @Test
    public void givenKingAndKnightVsKing_whenDetectGameEndCause_thenInsufficientMaterial() {
        testInsufficientMaterial(StandardPieceType.KNIGHT);
    }

    @Test
    public void givenKingAndQueenVsKing_whenDetectGameEndCause_thenNoGameEnd() {
        testSufficientMaterial(StandardPieceType.QUEEN);
    }

    @Test
    public void givenKingAndRookVsKing_whenDetectGameEndCause_thenNoGameEnd() {
        testSufficientMaterial(StandardPieceType.ROOK);
    }

    @Test
    public void givenKingAndMultipleBishopsVsKing_whenDetectGameEndCause_thenNoGameEnd() {
        testSufficientMaterial(StandardPieceType.BISHOP, StandardPieceType.BISHOP);
    }

    @Test
    public void givenKingAndMultipleKnightsVsKing_whenDetectGameEndCause_thenNoGameEnd() {
        testSufficientMaterial(StandardPieceType.KNIGHT, StandardPieceType.KNIGHT);
    }
}
