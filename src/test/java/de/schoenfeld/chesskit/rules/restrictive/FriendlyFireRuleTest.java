package de.schoenfeld.chesskit.rules.restrictive;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FriendlyFireRuleTest {
    private FriendlyFireRule<Square8x8, StandardPieceType> tested;
    private MoveLookup<Square8x8, StandardPieceType> moves;
    private GameState<Square8x8, StandardPieceType> state;
    private ChessBoard<Square8x8, StandardPieceType> board;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        tested = new FriendlyFireRule<>();
        moves = new MoveLookup<>();

        state = mock(GameState.class);
        board = mock(ChessBoard.class);

        when(state.getChessBoard()).thenReturn(board);
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
        ChessPiece<StandardPieceType> whitePawn = new ChessPiece<>(StandardPieceType.PAWN, Color.WHITE);
        ChessPiece<StandardPieceType> blackKnight = new ChessPiece<>(StandardPieceType.KNIGHT, Color.BLACK);

        Square8x8 from = Square8x8.of(2, 2);
        Square8x8 to = Square8x8.of(3, 3);

        Move<Square8x8, StandardPieceType> validMove = Move.of(whitePawn, from, to);

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
        ChessPiece<StandardPieceType> whitePawn = new ChessPiece<>(StandardPieceType.PAWN, Color.WHITE);
        ChessPiece<StandardPieceType> whiteKnight = new ChessPiece<>(StandardPieceType.KNIGHT, Color.BLACK);

        Square8x8 from = Square8x8.of(2, 2);
        Square8x8 to = Square8x8.of(3, 3);

        Move<Square8x8, StandardPieceType> invalidMove = Move.of(whitePawn, from, to);

        moves.add(invalidMove);
        when(board.getPieceAt(to)).thenReturn(whiteKnight); // Friendly piece at destination

        // When
        tested.filterMoves(moves, state);

        // Then
        assertTrue(moves.isEmpty(), "Expected friendly fire move to be removed.");
    }
}
