package de.schoenfeld.chesskit.rules.gameend;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.events.GameConclusion;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.StandardPieceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InsufficientMaterialDetectorTest {

    private void setupBoardMock(ChessBoard<Square8x8, StandardPieceType> board,
                                StandardPieceType... pieceTypes) {
        // Assume kings at default positions
        when(board.getTilesWithType(StandardPieceType.KING))
                .thenReturn(List.of(Square8x8.of(4, 0), Square8x8.of(4, 7)));

        for (StandardPieceType pieceType : pieceTypes) {
            List<Square8x8> square8x8s = board.getTilesWithType(pieceType);
            if (square8x8s == null) square8x8s = new ArrayList<>();
            square8x8s.add(Square8x8.of(0, 0));
            when(board.getTilesWithType(pieceType)).thenReturn(square8x8s);
        }
    }

    @SuppressWarnings("unchecked")
    private void testInsufficientMaterial(StandardPieceType... pieceTypes) {
        ChessBoard<Square8x8, StandardPieceType> board = mock(ChessBoard.class);
        GameState<Square8x8, StandardPieceType> gameState = mock(GameState.class);
        when(gameState.getChessBoard()).thenReturn(board);
        setupBoardMock(board, pieceTypes);

        InsufficientMaterialDetector<Square8x8> rule = new InsufficientMaterialDetector<>();
        GameConclusion conclusion = rule.detectConclusion(gameState);

        assertNotNull(conclusion);
        assertEquals("Insufficient material", conclusion.description());
        assertEquals(GameConclusion.Winner.NONE, conclusion.winner());
    }

    @SuppressWarnings("unchecked")
    private void testSufficientMaterial(StandardPieceType... pieceTypes) {
        ChessBoard<Square8x8, StandardPieceType> board = mock(ChessBoard.class);
        GameState<Square8x8, StandardPieceType> gameState = mock(GameState.class);
        when(gameState.getChessBoard()).thenReturn(board);
        setupBoardMock(board, pieceTypes);

        InsufficientMaterialDetector<Square8x8> rule = new InsufficientMaterialDetector<>();
        GameConclusion conclusion = rule.detectConclusion(gameState);

        assertNull(conclusion);
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
