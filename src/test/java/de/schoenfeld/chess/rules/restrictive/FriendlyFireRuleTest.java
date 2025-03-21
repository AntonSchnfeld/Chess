package de.schoenfeld.chess.rules.restrictive;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FriendlyFireRuleTest {
    private FriendlyFireRule tested;
    private MoveCollection moves;
    private GameState state;
    private ChessBoard board;

    @BeforeEach
    public void setup() {
        tested = new FriendlyFireRule();
        moves = new MoveCollection();

        state = mock(GameState.class);
        board = mock(ChessBoard.class);

        when(state.chessBoard()).thenReturn(board);
    }

    @Test
    public void givenNoMoves_whenFilterMoves_thenDontRemoveAnything() {
        // Given: an empty move collection
        // When
        tested.filterMoves(moves, state);

        // Then
        assertTrue(moves.isEmpty(), "Expected move collection to remain empty.");
    }

    @Test
    public void givenMovesWithoutFriendlyFire_whenFilterMoves_thenDontRemoveAnything() {
        // Given
        ChessPiece whitePawn = new ChessPiece(PieceType.PAWN, true);
        ChessPiece blackKnight = new ChessPiece(PieceType.KNIGHT, false);

        Square from = Square.of(2, 2);
        Square to = Square.of(3, 3);

        Move validMove = Move.of(whitePawn, from, to);

        moves.add(validMove);
        when(board.getPieceAt(to)).thenReturn(blackKnight); // Opponent piece at destination

        // When
        tested.filterMoves(moves, state);

        // Then
        assertEquals(1, moves.size(), "Expected valid move to remain in collection.");
    }

    @Test
    public void givenMovesWithFriendlyFire_whenFilterMoves_thenRemoveFriendlyMoves() {
        // Given
        ChessPiece whitePawn = new ChessPiece(PieceType.PAWN, true);
        ChessPiece whiteKnight = new ChessPiece(PieceType.KNIGHT, true);

        Square from = Square.of(2, 2);
        Square to = Square.of(3, 3);

        Move invalidMove = Move.of(whitePawn, from, to);

        moves.add(invalidMove);
        when(board.getPieceAt(to)).thenReturn(whiteKnight); // Friendly piece at destination

        // When
        tested.filterMoves(moves, state);

        // Then
        assertTrue(moves.isEmpty(), "Expected friendly fire move to be removed.");
    }
}
