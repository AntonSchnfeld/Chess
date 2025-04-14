package de.schoenfeld.chesskit.rules.restrictive;

import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.MoveGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CheckRuleTest {
    private CheckRule<Square8x8, StandardPieceType> checkRule;
    private MoveGenerator<Square8x8, StandardPieceType> moveGenerator;
    private GameState<Square8x8, StandardPieceType> gameState;
    private MoveLookup<Square8x8, StandardPieceType> moves;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        moveGenerator = mock(MoveGenerator.class);
        checkRule = new CheckRule<>(moveGenerator, StandardPieceType.KING);

        gameState = mock(GameState.class);
        moves = new MoveLookup<>();
    }

    @Test
    public void givenNoMoves_whenFilterMoves_thenNothingChanges() {
        assertTrue(moves.isEmpty());

        checkRule.filterMoves(moves, gameState);

        assertTrue(moves.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenLegalMove_whenFilterMoves_thenMoveRemains() {
        Move<Square8x8, StandardPieceType> legalMove = mock(Move.class);

        when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                .thenReturn(List.of(Square8x8.of(4, 0))); // King's position
        when(moveGenerator.generateMoves(gameState)).thenReturn(new MoveLookup<>()); // No checks

        moves.add(legalMove);

        checkRule.filterMoves(moves, gameState);

        assertFalse(moves.isEmpty());
        assertTrue(moves.contains(legalMove));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenIllegalMove_whenFilterMoves_thenMoveIsRemoved() {
        Move<Square8x8, StandardPieceType> illegalMove = mock(Move.class);

        when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                .thenReturn(List.of(Square8x8.of(4, 0))); // King's position

        MoveLookup<Square8x8, StandardPieceType> opponentMoves = MoveLookup.of(
                Move.of(mock(ChessPiece.class), Square8x8.of(3, 2), Square8x8.of(4, 0)) // Attacking the king
        );
        when(moveGenerator.generateMoves(gameState)).thenReturn(opponentMoves);

        moves.add(illegalMove);

        checkRule.filterMoves(moves, gameState);

        assertTrue(moves.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenMixedMoves_whenFilterMoves_thenOnlyIllegalMovesAreRemoved() {
        Move<Square8x8, StandardPieceType> legalMove = mock(Move.class);
        Move<Square8x8, StandardPieceType> illegalMove = mock(Move.class);

        when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                .thenReturn(List.of(Square8x8.of(4, 0)));

        MoveLookup<Square8x8, StandardPieceType> opponentMoves = mock(MoveLookup.class);
        when(opponentMoves.containsMoveTo(Square8x8.of(4, 0))).thenReturn(true);
        when(moveGenerator.generateMoves(gameState)).thenReturn(opponentMoves);

        doAnswer(invocation -> {
            when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                    .thenReturn(List.of(Square8x8.of(4, 1))); // Move king to a safe square
            return null;
        }).when(gameState).makeMove(legalMove);

        doAnswer(invocation -> {
            when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                    .thenReturn(List.of(Square8x8.of(4, 0))); // King stays in check
            return null;
        }).when(gameState).makeMove(illegalMove);

        doAnswer(invocation -> {
            when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                    .thenReturn(List.of(Square8x8.of(4, 0))); // Restore original state
            return null;
        }).when(gameState).unmakeLastMove();

        doAnswer(invocation -> {
            when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                    .thenReturn(List.of(Square8x8.of(4, 0)));
            return null;
        }).when(gameState).unmakeLastMove();

        moves.add(legalMove);
        moves.add(illegalMove);

        checkRule.filterMoves(moves, gameState);

        assertEquals(1, moves.size());
        assertTrue(moves.contains(legalMove));
        assertFalse(moves.contains(illegalMove));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenPinnedPieceMove_whenFilterMoves_thenPinnedMoveIsRemoved() {
        Move<Square8x8, StandardPieceType> pinnedMove = mock(Move.class);

        when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                .thenReturn(List.of(Square8x8.of(4, 0)));

        MoveLookup<Square8x8, StandardPieceType> opponentMoves = mock(MoveLookup.class);
        when(opponentMoves.containsMoveTo(Square8x8.of(4, 0))).thenReturn(true);
        when(moveGenerator.generateMoves(gameState)).thenReturn(opponentMoves);

        doAnswer(invocation -> {
            when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                    .thenReturn(List.of(Square8x8.of(4, 0))); // King remains in danger (pinned piece moves)
            return null;
        }).when(gameState).makeMove(pinnedMove);

        doAnswer(invocation -> {
            when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                    .thenReturn(List.of(Square8x8.of(4, 0))); // Restore original state
            return null;
        }).when(gameState).unmakeLastMove();

        moves.add(pinnedMove);

        checkRule.filterMoves(moves, gameState);

        assertTrue(moves.isEmpty()); // Pinned move should be removed
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenKingMoves_whenFilterMoves_thenOnlySafeKingMovesRemain() {
        Move<Square8x8, StandardPieceType> safeKingMove = mock(Move.class);
        Move<Square8x8, StandardPieceType> unsafeKingMove = mock(Move.class);

        when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                .thenReturn(List.of(Square8x8.of(4, 0)));

        MoveLookup<Square8x8, StandardPieceType> opponentMoves = mock(MoveLookup.class);
        when(opponentMoves.containsMoveTo(Square8x8.of(4, 1))).thenReturn(false); // e2 is safe
        when(opponentMoves.containsMoveTo(Square8x8.of(3, 0))).thenReturn(true);  // d1 is attacked
        when(moveGenerator.generateMoves(gameState)).thenReturn(opponentMoves);

        doAnswer(invocation -> {
            when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                    .thenReturn(List.of(Square8x8.of(4, 1))); // Move king to e2
            return null;
        }).when(gameState).makeMove(safeKingMove);

        doAnswer(invocation -> {
            when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                    .thenReturn(List.of(Square8x8.of(3, 0))); // Move king to d1 (unsafe)
            return null;
        }).when(gameState).makeMove(unsafeKingMove);

        doAnswer(invocation -> {
            when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                    .thenReturn(List.of(Square8x8.of(4, 0))); // Restore original state
            return null;
        }).when(gameState).unmakeLastMove();

        doAnswer(invocation -> {
            when(gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()))
                    .thenReturn(List.of(Square8x8.of(3, 0)));
            return null;
        }).when(gameState).unmakeLastMove();

        moves.add(safeKingMove);
        moves.add(unsafeKingMove);

        checkRule.filterMoves(moves, gameState);

        assertEquals(1, moves.size());
        assertTrue(moves.contains(safeKingMove));
        assertFalse(moves.contains(unsafeKingMove));
    }
}
