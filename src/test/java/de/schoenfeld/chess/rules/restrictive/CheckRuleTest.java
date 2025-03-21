package de.schoenfeld.chess.rules.restrictive;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.*;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.MoveGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckRuleTest {
    private CheckRule checkRule;
    private MoveGenerator<StandardPieceType> moveGenerator;
    private GameState<StandardPieceType> gameState;
    private MoveCollection moves;

    @BeforeEach
    public void setup() {
        moveGenerator = mock(MoveGenerator.class);
        checkRule = new CheckRule(moveGenerator);

        gameState = mock(GameState.class);
        moves = new MoveCollection();
    }

    @Test
    public void givenNoMoves_whenFilterMoves_thenNothingChanges() {
        // Given an empty MoveCollection
        assertTrue(moves.isEmpty());

        // When filtering
        checkRule.filterMoves(moves, gameState);

        // Then nothing should be changed
        assertTrue(moves.isEmpty());
    }

    @Test
    public void givenLegalMove_whenFilterMoves_thenMoveRemains() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        Move legalMove = mock(Move.class);
        GameState<StandardPieceType> futureState = mock(GameState.class);

        when(legalMove.executeOn(gameState)).thenReturn(futureState);
        when(futureState.chessBoard()).thenReturn(mock(ChessBoard.class));
        when(futureState.chessBoard().getPiecesOfTypeAndColour(StandardPieceType.KING, gameState.isWhiteTurn()))
                .thenReturn(List.of(mock(ChessPiece.class)));
        when(moveGenerator.generateMoves(futureState)).thenReturn(new MoveCollection()); // No attacks on king

        moves.add(legalMove);

        // When
        checkRule.filterMoves(moves, gameState);

        // Then the move should not be removed
        assertFalse(moves.isEmpty());
        assertTrue(moves.contains(legalMove));
    }

    @Test
    public void givenIllegalMove_whenFilterMoves_thenMoveIsRemoved() {
        // Given
        ChessPiece piece = mock(ChessPiece.class);
        Move illegalMove = mock(Move.class);
        GameState<StandardPieceType> futureState = mock(GameState.class);
        ChessBoard<StandardPieceType> futureBoard = mock(ChessBoard.class);
        ChessPiece king = mock(ChessPiece.class);

        when(illegalMove.executeOn(gameState)).thenReturn(futureState);
        when(futureState.chessBoard()).thenReturn(futureBoard);
        when(futureBoard.getPiecesOfTypeAndColour(StandardPieceType.KING, gameState.isWhiteTurn()))
                .thenReturn(List.of(king));
        when(futureBoard.getPiecePosition(king)).thenReturn(Square.of(4, 4)); // King at (4,4)

        MoveCollection futureMoves = new MoveCollection();
        futureMoves.add(Move.of(mock(ChessPiece.class), Square.of(2, 2), Square.of(4, 4))); // Attack on king
        when(moveGenerator.generateMoves(futureState)).thenReturn(futureMoves);

        moves.add(illegalMove);

        // When
        checkRule.filterMoves(moves, gameState);

        // Then the move should be removed
        assertTrue(moves.isEmpty());
    }

    @Test
    public void givenMixedMoves_whenFilterMoves_thenOnlyIllegalMovesAreRemoved() {
        // Given
        Move legalMove = mock(Move.class);
        Move illegalMove = mock(Move.class);
        GameState<StandardPieceType> futureStateLegal = mock(GameState.class);
        GameState<StandardPieceType> futureStateIllegal = mock(GameState.class);
        ChessBoard<StandardPieceType> futureBoardLegal = mock(ChessBoard.class);
        ChessBoard<StandardPieceType> futureBoardIllegal = mock(ChessBoard.class);
        ChessPiece king = mock(ChessPiece.class);

        // Set up legal move scenario
        when(legalMove.executeOn(gameState)).thenReturn(futureStateLegal);
        when(futureStateLegal.chessBoard()).thenReturn(futureBoardLegal);
        when(futureBoardLegal.getPiecesOfTypeAndColour(StandardPieceType.KING, gameState.isWhiteTurn()))
                .thenReturn(List.of(mock(ChessPiece.class)));
        when(moveGenerator.generateMoves(futureStateLegal)).thenReturn(new MoveCollection()); // No attack on king

        // Set up illegal move scenario
        when(illegalMove.executeOn(gameState)).thenReturn(futureStateIllegal);
        when(futureStateIllegal.chessBoard()).thenReturn(futureBoardIllegal);
        when(futureBoardIllegal.getPiecesOfTypeAndColour(StandardPieceType.KING, gameState.isWhiteTurn()))
                .thenReturn(List.of(king));
        when(futureBoardIllegal.getPiecePosition(king)).thenReturn(Square.of(4, 4));

        MoveCollection futureMoves = new MoveCollection();
        futureMoves.add(Move.of(mock(ChessPiece.class), Square.of(2, 2), Square.of(4, 4))); // Attack on king
        when(moveGenerator.generateMoves(futureStateIllegal)).thenReturn(futureMoves);

        moves.add(legalMove);
        moves.add(illegalMove);

        // When
        checkRule.filterMoves(moves, gameState);

        // Then only the illegal move should be removed
        assertEquals(1, moves.size());
        assertTrue(moves.contains(legalMove));
        assertFalse(moves.contains(illegalMove));
    }
}
