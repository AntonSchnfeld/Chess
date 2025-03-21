package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
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
        ChessBoard board = mock(ChessBoard.class);
        GameState gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);

        ChessPiece whiteKing = new ChessPiece(PieceType.KING, true);
        ChessPiece blackKing = new ChessPiece(PieceType.KING, false);
        when(board.getPiecesOfType(PieceType.KING)).thenReturn(List.of(whiteKing, blackKing));

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
        ChessBoard board = mock(ChessBoard.class);
        GameState gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);

        ChessPiece whiteKing = new ChessPiece(PieceType.KING, true);
        ChessPiece whiteBishop = new ChessPiece(PieceType.BISHOP, true);
        ChessPiece blackKing = new ChessPiece(PieceType.KING, false);
        when(board.getPiecesOfType(PieceType.KING)).thenReturn(List.of(whiteKing, blackKing));
        when(board.getPiecesOfType(PieceType.BISHOP)).thenReturn(List.of(whiteBishop));

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
        ChessBoard board = mock(ChessBoard.class);
        GameState gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);

        ChessPiece whiteKing = new ChessPiece(PieceType.KING, true);
        ChessPiece whiteKnight = new ChessPiece(PieceType.KNIGHT, true);
        ChessPiece blackKing = new ChessPiece(PieceType.KING, false);
        when(board.getPiecesOfType(PieceType.KING)).thenReturn(List.of(whiteKing, blackKing));
        when(board.getPiecesOfType(PieceType.KNIGHT)).thenReturn(List.of(whiteKnight));

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
        ChessBoard board = mock(ChessBoard.class);
        GameState gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);

        ChessPiece whiteKing = new ChessPiece(PieceType.KING, true);
        ChessPiece whiteQueen = new ChessPiece(PieceType.QUEEN, true);
        ChessPiece blackKing = new ChessPiece(PieceType.KING, false);
        when(board.getPiecesOfType(PieceType.KING)).thenReturn(List.of(whiteKing, blackKing));
        when(board.getPiecesOfType(PieceType.QUEEN)).thenReturn(List.of(whiteQueen));

        // Act: Check for game end cause
        InsufficientMaterialRule rule = new InsufficientMaterialRule();
        Optional<GameConclusion> conclusion = rule.detectGameEndCause(gameState);

        // Assert: Game can continue (no insufficient material)
        assertFalse(conclusion.isPresent());
    }

    @Test
    public void givenKingAndRookVsKing_whenDetectGameEndCause_thenNoGameEnd() {
        // Arrange: Mock the board and game state
        ChessBoard board = mock(ChessBoard.class);
        GameState gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);

        ChessPiece whiteKing = new ChessPiece(PieceType.KING, true);
        ChessPiece whiteRook = new ChessPiece(PieceType.ROOK, true);
        ChessPiece blackKing = new ChessPiece(PieceType.KING, false);
        when(board.getPiecesOfType(PieceType.KING)).thenReturn(List.of(whiteKing, blackKing));
        when(board.getPiecesOfType(PieceType.ROOK)).thenReturn(List.of(whiteRook));

        // Act: Check for game end cause
        InsufficientMaterialRule rule = new InsufficientMaterialRule();
        Optional<GameConclusion> conclusion = rule.detectGameEndCause(gameState);

        // Assert: Game can continue (no insufficient material)
        assertFalse(conclusion.isPresent());
    }

    @Test
    public void givenKingAndMultipleBishopsVsKing_whenDetectGameEndCause_thenNoGameEnd() {
        // Arrange: Mock the board and game state
        ChessBoard board = mock(ChessBoard.class);
        GameState gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);

        ChessPiece whiteKing = new ChessPiece(PieceType.KING, true);
        ChessPiece whiteBishop1 = new ChessPiece(PieceType.BISHOP, true);
        ChessPiece whiteBishop2 = new ChessPiece(PieceType.BISHOP, true);
        ChessPiece blackKing = new ChessPiece(PieceType.KING, false);
        when(board.getPiecesOfType(PieceType.KING)).thenReturn(List.of(whiteKing, blackKing));
        when(board.getPiecesOfType(PieceType.BISHOP)).thenReturn(List.of(whiteBishop1, whiteBishop2));

        // Act: Check for game end cause
        InsufficientMaterialRule rule = new InsufficientMaterialRule();
        Optional<GameConclusion> conclusion = rule.detectGameEndCause(gameState);

        // Assert: Game can continue (no insufficient material)
        assertFalse(conclusion.isPresent());
    }

    @Test
    public void givenKingAndMultipleKnightsVsKing_whenDetectGameEndCause_thenNoGameEnd() {
        // Arrange: Mock the board and game state
        ChessBoard board = mock(ChessBoard.class);
        GameState gameState = mock(GameState.class);
        when(gameState.chessBoard()).thenReturn(board);

        ChessPiece whiteKing = new ChessPiece(PieceType.KING, true);
        ChessPiece whiteKnight1 = new ChessPiece(PieceType.KNIGHT, true);
        ChessPiece whiteKnight2 = new ChessPiece(PieceType.KNIGHT, true);
        ChessPiece blackKing = new ChessPiece(PieceType.KING, false);
        when(board.getPiecesOfType(PieceType.KING)).thenReturn(List.of(whiteKing, blackKing));
        when(board.getPiecesOfType(PieceType.KNIGHT)).thenReturn(List.of(whiteKnight1, whiteKnight2));

        // Act: Check for game end cause
        InsufficientMaterialRule rule = new InsufficientMaterialRule();
        Optional<GameConclusion> conclusion = rule.detectGameEndCause(gameState);

        // Assert: Game can continue (no insufficient material)
        assertFalse(conclusion.isPresent());
    }
}
