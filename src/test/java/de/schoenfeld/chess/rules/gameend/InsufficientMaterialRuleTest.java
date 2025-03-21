package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.StandardPieceType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InsufficientMaterialRuleTest {

    @Test
    public void givenKingVsKing_whenDetectGameEndCause_thenInsufficientMaterial() {
        // Arrange: Mock the board and game state
        ChessBoard<StandardPieceType> board = mock(ChessBoard.class);
        GameState<StandardPieceType> gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);

        ChessPiece<StandardPieceType> whiteKing = new ChessPiece<>(StandardPieceType.KING, true);
        ChessPiece<StandardPieceType> blackKing = new ChessPiece<>(StandardPieceType.KING, false);
        when(board.getPiecesOfType(StandardPieceType.KING)).thenReturn(List.of(whiteKing, blackKing));

        // Act: Check for game end cause
        InsufficientMaterialRule rule = new InsufficientMaterialRule();
        Optional<GameConclusion> conclusion = rule.detectGameEndCause(gameState);

        // Assert: There is insufficient material
        assertTrue(conclusion.isPresent());
        assertEquals("Insufficient material", conclusion.get().description());
        assertEquals(GameConclusion.Winner.NONE, conclusion.get().winner());
    }

    @Test
    public void givenKingAndBishopVsKing_whenDetectGameEndCause_thenInsufficientMaterial() {
        // Arrange: Mock the board and game state
        ChessBoard<StandardPieceType> board = mock(ChessBoard.class);
        GameState<StandardPieceType> gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);

        ChessPiece<StandardPieceType> whiteKing = new ChessPiece<>(StandardPieceType.KING, true);
        ChessPiece<StandardPieceType> whiteBishop = new ChessPiece<>(StandardPieceType.BISHOP, true);
        ChessPiece<StandardPieceType> blackKing = new ChessPiece<>(StandardPieceType.KING, false);
        when(board.getPiecesOfType(StandardPieceType.KING)).thenReturn(List.of(whiteKing, blackKing));
        when(board.getPiecesOfType(StandardPieceType.BISHOP)).thenReturn(List.of(whiteBishop));

        // Act: Check for game end cause
        InsufficientMaterialRule rule = new InsufficientMaterialRule();
        Optional<GameConclusion> conclusion = rule.detectGameEndCause(gameState);

        // Assert: There is insufficient material
        assertTrue(conclusion.isPresent());
        assertEquals("Insufficient material", conclusion.get().description());
        assertEquals(GameConclusion.Winner.NONE, conclusion.get().winner());
    }

    @Test
    public void givenKingAndKnightVsKing_whenDetectGameEndCause_thenInsufficientMaterial() {
        // Arrange: Mock the board and game state
        ChessBoard<StandardPieceType> board = mock(ChessBoard.class);
        GameState<StandardPieceType> gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);

        ChessPiece<StandardPieceType> whiteKing = new ChessPiece<>(StandardPieceType.KING, true);
        ChessPiece<StandardPieceType> whiteKnight = new ChessPiece<>(StandardPieceType.KNIGHT, true);
        ChessPiece<StandardPieceType> blackKing = new ChessPiece<>(StandardPieceType.KING, false);
        when(board.getPiecesOfType(StandardPieceType.KING)).thenReturn(List.of(whiteKing, blackKing));
        when(board.getPiecesOfType(StandardPieceType.KNIGHT)).thenReturn(List.of(whiteKnight));

        // Act: Check for game end cause
        InsufficientMaterialRule rule = new InsufficientMaterialRule();
        Optional<GameConclusion> conclusion = rule.detectGameEndCause(gameState);

        // Assert: There is insufficient material
        assertTrue(conclusion.isPresent());
        assertEquals("Insufficient material", conclusion.get().description());
        assertEquals(GameConclusion.Winner.NONE, conclusion.get().winner());
    }

    @Test
    public void givenKingAndQueenVsKing_whenDetectGameEndCause_thenNoGameEnd() {
        // Arrange: Mock the board and game state
        ChessBoard<StandardPieceType> board = mock(ChessBoard.class);
        GameState<StandardPieceType> gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);

        ChessPiece<StandardPieceType> whiteKing = new ChessPiece<>(StandardPieceType.KING, true);
        ChessPiece<StandardPieceType> whiteQueen = new ChessPiece<>(StandardPieceType.QUEEN, true);
        ChessPiece<StandardPieceType> blackKing = new ChessPiece<>(StandardPieceType.KING, false);
        when(board.getPiecesOfType(StandardPieceType.KING)).thenReturn(List.of(whiteKing, blackKing));
        when(board.getPiecesOfType(StandardPieceType.QUEEN)).thenReturn(List.of(whiteQueen));

        // Act: Check for game end cause
        InsufficientMaterialRule rule = new InsufficientMaterialRule();
        Optional<GameConclusion> conclusion = rule.detectGameEndCause(gameState);

        // Assert: Game can continue (no insufficient material)
        assertFalse(conclusion.isPresent());
    }

    @Test
    public void givenKingAndRookVsKing_whenDetectGameEndCause_thenNoGameEnd() {
        // Arrange: Mock the board and game state
        ChessBoard<StandardPieceType> board = mock(ChessBoard.class);
        GameState<StandardPieceType> gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);

        ChessPiece<StandardPieceType> whiteKing = new ChessPiece<>(StandardPieceType.KING, true);
        ChessPiece<StandardPieceType> whiteRook = new ChessPiece<>(StandardPieceType.ROOK, true);
        ChessPiece<StandardPieceType> blackKing = new ChessPiece<>(StandardPieceType.KING, false);
        when(board.getPiecesOfType(StandardPieceType.KING)).thenReturn(List.of(whiteKing, blackKing));
        when(board.getPiecesOfType(StandardPieceType.ROOK)).thenReturn(List.of(whiteRook));

        // Act: Check for game end cause
        InsufficientMaterialRule rule = new InsufficientMaterialRule();
        Optional<GameConclusion> conclusion = rule.detectGameEndCause(gameState);

        // Assert: Game can continue (no insufficient material)
        assertFalse(conclusion.isPresent());
    }

    @Test
    public void givenKingAndMultipleBishopsVsKing_whenDetectGameEndCause_thenNoGameEnd() {
        // Arrange: Mock the board and game state
        ChessBoard<StandardPieceType> board = mock(ChessBoard.class);
        GameState<StandardPieceType> gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);

        ChessPiece<StandardPieceType> whiteKing = new ChessPiece<>(StandardPieceType.KING, true);
        ChessPiece<StandardPieceType> whiteBishop1 = new ChessPiece<>(StandardPieceType.BISHOP, true);
        ChessPiece<StandardPieceType> whiteBishop2 = new ChessPiece<>(StandardPieceType.BISHOP, true);
        ChessPiece<StandardPieceType> blackKing = new ChessPiece<>(StandardPieceType.KING, false);
        when(board.getPiecesOfType(StandardPieceType.KING)).thenReturn(List.of(whiteKing, blackKing));
        when(board.getPiecesOfType(StandardPieceType.BISHOP)).thenReturn(List.of(whiteBishop1, whiteBishop2));

        // Act: Check for game end cause
        InsufficientMaterialRule rule = new InsufficientMaterialRule();
        Optional<GameConclusion> conclusion = rule.detectGameEndCause(gameState);

        // Assert: Game can continue (no insufficient material)
        assertFalse(conclusion.isPresent());
    }

    @Test
    public void givenKingAndMultipleKnightsVsKing_whenDetectGameEndCause_thenNoGameEnd() {
        // Arrange: Mock the board and game state
        ChessBoard<StandardPieceType> board = mock(ChessBoard.class);
        GameState<StandardPieceType> gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);

        ChessPiece<StandardPieceType> whiteKing = new ChessPiece<>(StandardPieceType.KING, true);
        ChessPiece<StandardPieceType> whiteKnight1 = new ChessPiece<>(StandardPieceType.KNIGHT, true);
        ChessPiece<StandardPieceType> whiteKnight2 = new ChessPiece<>(StandardPieceType.KNIGHT, true);
        ChessPiece<StandardPieceType> blackKing = new ChessPiece<>(StandardPieceType.KING, false);
        when(board.getPiecesOfType(StandardPieceType.KING)).thenReturn(List.of(whiteKing, blackKing));
        when(board.getPiecesOfType(StandardPieceType.KNIGHT)).thenReturn(List.of(whiteKnight1, whiteKnight2));

        // Act: Check for game end cause
        InsufficientMaterialRule rule = new InsufficientMaterialRule();
        Optional<GameConclusion> conclusion = rule.detectGameEndCause(gameState);

        // Assert: Game can continue (no insufficient material)
        assertFalse(conclusion.isPresent());
    }
}
