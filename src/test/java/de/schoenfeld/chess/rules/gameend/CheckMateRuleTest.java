package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.board.MapChessBoard;
import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.*;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.MoveGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CheckMateRuleTest {
    private CheckMateRule tested;
    private MoveGenerator<StandardPieceType> moveGenerator;
    private GameState<StandardPieceType> gameState;
    private ChessBoard<StandardPieceType> board;
    private ChessPiece king;
    private ChessPiece secondKing;

    @BeforeEach
    public void setup() {
        moveGenerator = mock(MoveGenerator.class);
        tested = new CheckMateRule(moveGenerator);

        board = new MapChessBoard<>(new ChessBoardBounds(8, 8));
        gameState = new GameState<>(board);
        king = new ChessPiece(StandardPieceType.KING, true);
        secondKing = new ChessPiece(StandardPieceType.KING, true);
    }

    @Test
    public void givenNoLegalMovesAndKingAttacked_whenDetectGameEndCause_thenReturnCheckmate() {
        when(moveGenerator.generateMoves(gameState)).thenReturn(new MoveCollection());
        gameState = gameState.withPieceAt(king, Square.h5);

        when(moveGenerator.generateMoves(gameState.withTurnSwitched()))
                .thenReturn(MoveCollection.of(Move.of(mock(ChessPiece.class), Square.of(3, 1), Square.of(4, 0))));
        when(moveGenerator.generateMoves(gameState)).thenReturn(MoveCollection.of());

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isPresent());
        assertEquals(GameConclusion.Winner.BLACK, result.get().winner());
        assertEquals("Checkmate", result.get().description());
    }

    @Test
    public void givenNoLegalMovesAndKingNotAttacked_whenDetectGameEndCause_thenReturnEmpty() {
        when(moveGenerator.generateMoves(gameState)).thenReturn(new MoveCollection());
        gameState = gameState.withPieceAt(king, Square.h5);

        GameState<StandardPieceType> enemyState = gameState.withIsWhiteTurn(!gameState.isWhiteTurn());
        when(moveGenerator.generateMoves(enemyState)).thenReturn(new MoveCollection());

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenLegalMoves_whenDetectGameEndCause_thenReturnEmpty() {
        when(moveGenerator.generateMoves(gameState))
                .thenReturn(MoveCollection.of(Move.of(mock(ChessPiece.class), Square.of(4, 0), Square.of(4, 1))));

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenKingMissing_whenDetectGameEndCause_thenReturnEmpty() {
        when(moveGenerator.generateMoves(gameState)).thenReturn(new MoveCollection());

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenMultipleKingsOneInCheckAndNoLegalMoves_whenDetectGameEndCause_thenReturnCheckmate() {
        gameState = gameState.withPieceAt(king, Square.h5).withPieceAt(secondKing, Square.h7);

        GameState<StandardPieceType> enemyState = gameState.withIsWhiteTurn(!gameState.isWhiteTurn());
        when(moveGenerator.generateMoves(enemyState))
                .thenReturn(MoveCollection.of(
                        Move.of(mock(ChessPiece.class), Square.of(3, 1), Square.of(4, 0)),
                        Move.of(mock(ChessPiece.class), Square.of(7, 1), Square.of(6, 0))));
        when(moveGenerator.generateMoves(gameState)).thenReturn(MoveCollection.of());

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isPresent());
        assertEquals(GameConclusion.Winner.BLACK, result.get().winner());
        assertEquals("Checkmate", result.get().description());
    }

    @Test
    public void givenMultipleKingsAndLegalMove_whenDetectGameEndCause_thenReturnEmpty() {
        when(moveGenerator.generateMoves(any())).thenReturn(new MoveCollection());
        when(moveGenerator.generateMoves(gameState))
                .thenReturn(MoveCollection.of(Move.of(king, Square.h5, Square.g5)));

        gameState = gameState.withPieceAt(king, Square.h5).withPieceAt(secondKing, Square.h7);

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenBothKingsInCheckAndNoLegalMoves_whenDetectGameEndCause_thenReturnCheckmate() {
        when(moveGenerator.generateMoves(gameState)).thenReturn(new MoveCollection());
        gameState = gameState.withPieceAt(king, Square.h5).withPieceAt(secondKing, Square.h7);

        when(moveGenerator.generateMoves(gameState.withTurnSwitched()))
                .thenReturn(MoveCollection.of(
                        Move.of(mock(ChessPiece.class), Square.of(3, 1), Square.of(4, 0)),
                        Move.of(mock(ChessPiece.class), Square.of(7, 1), Square.of(6, 0))));
        when(moveGenerator.generateMoves(gameState)).thenReturn(MoveCollection.of());

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isPresent());
        assertEquals(GameConclusion.Winner.BLACK, result.get().winner());
        assertEquals("Checkmate", result.get().description());
    }

    @Test
    public void givenWhiteKingOnA8AndMated_whenDetectGameEndCause_thenReturnCheckmate() {
        ChessPiece checkingPiece = new ChessPiece(StandardPieceType.ROOK, false);

        gameState = gameState.withPieceAt(king, Square.a8).withPieceAt(checkingPiece, Square.a1);

        MoveCollection enemyMoves = MoveCollection.of(
                Move.of(checkingPiece, Square.a1, Square.a8),
                Move.of(checkingPiece, Square.a1, Square.a7)
        );
        Move kingMove = Move.of(king, Square.a8, Square.a7);

        GameState<StandardPieceType> stateAfterKingMove = kingMove.executeOn(gameState);
        when(moveGenerator.generateMoves(gameState)).thenReturn(MoveCollection.of(kingMove));
        when(moveGenerator.generateMoves(gameState.withTurnSwitched())).thenReturn(enemyMoves);

        when(moveGenerator.generateMoves(stateAfterKingMove)).thenReturn(enemyMoves);
        when(moveGenerator.generateMoves(stateAfterKingMove.withIsWhiteTurn(true)))
                .thenReturn(MoveCollection.of());

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isPresent());
        assertEquals(GameConclusion.Winner.BLACK, result.get().winner());
        assertEquals("Checkmate", result.get().description());
    }
}
