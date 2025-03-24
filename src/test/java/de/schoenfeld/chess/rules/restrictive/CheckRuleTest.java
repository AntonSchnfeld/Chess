package de.schoenfeld.chess.rules.restrictive;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.MoveGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckRuleTest {
    private CheckRule checkRule;
    private MoveGenerator<StandardPieceType> moveGenerator;
    private GameState<StandardPieceType> gameState;
    private MoveCollection<StandardPieceType> moves;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        moveGenerator = mock(MoveGenerator.class);
        checkRule = new CheckRule(moveGenerator);

        gameState = mock(GameState.class);
        moves = new MoveCollection<>();
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
    @SuppressWarnings("unchecked")
    public void givenLegalMove_whenFilterMoves_thenMoveRemains() {
        // Given
        Move<StandardPieceType> legalMove = mock(Move.class);
        GameState<StandardPieceType> futureState = mock(GameState.class);
        ChessBoard<StandardPieceType> futureBoard = mock(ChessBoard.class);

        when(legalMove.executeOn(gameState)).thenReturn(futureState);
        when(futureState.chessBoard()).thenReturn(futureBoard);
        when(futureBoard.getSquaresWithTypeAndColour(StandardPieceType.KING, gameState.isWhiteTurn()))
                .thenReturn(List.of(Square.a1)); // Example square, replace with actual logic
        when(moveGenerator.generateMoves(futureState)).thenReturn(new MoveCollection<>()); // No attacks on king

        moves.add(legalMove);

        // When
        checkRule.filterMoves(moves, gameState);

        // Then the move should not be removed
        assertFalse(moves.isEmpty());
        assertTrue(moves.contains(legalMove));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenIllegalMove_whenFilterMoves_thenMoveIsRemoved() {
        // Given
        Move<StandardPieceType> illegalMove = mock(Move.class);
        GameState<StandardPieceType> futureState = mock(GameState.class);
        Square kingSquare = Square.a1;

        when(illegalMove.executeOn(gameState)).thenReturn(futureState);
        when(
                futureState.getSquaresWithTypeAndColour(StandardPieceType.KING,
                        gameState.isWhiteTurn())
        ).thenReturn(List.of(kingSquare)); // Check found
        // Return check move
        when(moveGenerator.generateMoves(futureState)).thenReturn(MoveCollection.of(
                Move.of(mock(ChessPiece.class), Square.f3, Square.a1)
        ));

        moves.add(illegalMove);

        // When
        checkRule.filterMoves(moves, gameState);

        // Then the move should be removed
        assertTrue(moves.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenMixedMoves_whenFilterMoves_thenOnlyIllegalMovesAreRemoved() {
        // Given
        Move<StandardPieceType> legalMove = mock(Move.class);
        Move<StandardPieceType> illegalMove = mock(Move.class);

        GameState<StandardPieceType> futureStateLegal = mock(GameState.class);
        GameState<StandardPieceType> futureStateIllegal = mock(GameState.class);

        Square kingPos = Square.a1;

        // Set up the legal move scenario
        setupLegalMoveScenario(legalMove, futureStateLegal, kingPos);

        // Set up the illegal move scenario
        setupIllegalMoveScenario(illegalMove, futureStateIllegal, kingPos);

        // Add moves to collection
        moves.add(legalMove);
        moves.add(illegalMove);

        // When
        checkRule.filterMoves(moves, gameState);

        // Then only the illegal move should be removed
        assertEquals(1, moves.size());
        assertTrue(moves.contains(legalMove));
        assertFalse(moves.contains(illegalMove));
    }

    private void setupLegalMoveScenario(Move<StandardPieceType> move,
                                        GameState<StandardPieceType> futureState,
                                        Square kingPos) {
        when(move.executeOn(gameState)).thenReturn(futureState);
        when(futureState.chessBoard()).thenReturn(futureState);
        when(futureState.getSquaresWithTypeAndColour(StandardPieceType.KING, gameState.isWhiteTurn()))
                .thenReturn(List.of(kingPos)); // Legal move, king is not under attack
        when(moveGenerator.generateMoves(futureState)).thenReturn(MoveCollection.of()); // No attack on king
    }

    @SuppressWarnings("unchecked")
    private void setupIllegalMoveScenario(Move<StandardPieceType> move,
                                          GameState<StandardPieceType> futureState,
                                          Square kingPos) {
        when(move.executeOn(gameState)).thenReturn(futureState);
        when(futureState.chessBoard()).thenReturn(futureState);
        when(futureState.getSquaresWithTypeAndColour(StandardPieceType.KING, gameState.isWhiteTurn()))
                .thenReturn(List.of(kingPos)); // King is present, simulate check scenario

        // Add a move that attacks the king
        ChessPiece<StandardPieceType> attackingPiece = mock(ChessPiece.class);
        Move<StandardPieceType> attackMove = Move.of(attackingPiece, Square.f3, kingPos);  // Simulate attack on king
        when(moveGenerator.generateMoves(futureState)).thenReturn(MoveCollection.of(attackMove)); // Attack on king
    }
}
