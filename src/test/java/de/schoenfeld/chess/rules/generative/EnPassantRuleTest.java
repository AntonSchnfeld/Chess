package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.model.*;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnPassantRuleTest {
    private EnPassantRule tested;
    private GameState<StandardPieceType> gameState;
    private MoveHistory<StandardPieceType> moveHistory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        tested = new EnPassantRule();
        gameState = mock(GameState.class);
        moveHistory = mock(MoveHistory.class);

        when(gameState.getMoveHistory()).thenReturn(moveHistory);
    }

    @Test
    public void givenNoMoveHistory_whenGenerateMoves_thenNoEnPassant() {
        when(moveHistory.getMoveCount()).thenReturn(0);

        MoveCollection<StandardPieceType> moves = tested.generateMoves(gameState);

        assertTrue(moves.isEmpty(), "En passant should not be possible without move history.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenLastMoveNotPawn_whenGenerateMoves_thenNoEnPassant() {
        when(moveHistory.getMoveCount()).thenReturn(1);

        Move<StandardPieceType> lastMove = mock(Move.class);
        ChessPiece<StandardPieceType> nonPawn = mock(ChessPiece.class);
        when(nonPawn.pieceType()).thenReturn(StandardPieceType.KNIGHT); // Not a pawn

        when(lastMove.movedPiece()).thenReturn(nonPawn);
        when(moveHistory.getLastMove()).thenReturn(lastMove);

        MoveCollection<StandardPieceType> moves = tested.generateMoves(gameState);

        assertTrue(moves.isEmpty(), "En passant should not be possible if the last move was not a pawn move.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenLastMoveNotDoublePawnMove_whenGenerateMoves_thenNoEnPassant() {
        when(moveHistory.getMoveCount()).thenReturn(1);

        Move<StandardPieceType> lastMove = mock(Move.class);
        ChessPiece<StandardPieceType> pawn = mock(ChessPiece.class);
        when(pawn.pieceType()).thenReturn(StandardPieceType.PAWN);

        when(lastMove.movedPiece()).thenReturn(pawn);
        when(lastMove.from()).thenReturn(Square.of(4, 2));
        when(lastMove.to()).thenReturn(Square.of(4, 3)); // Single move

        when(moveHistory.getLastMove()).thenReturn(lastMove);

        MoveCollection<StandardPieceType> moves = tested.generateMoves(gameState);

        assertTrue(moves.isEmpty(), "En passant should not be possible if the last pawn move was not a double move.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenValidEnPassantForWhite_whenGenerateMoves_thenMoveIsGenerated() {
        // Set up move history to indicate a Black pawn double move.
        when(moveHistory.getMoveCount()).thenReturn(1);

        // Create a Black pawn that made the double move.
        ChessPiece<StandardPieceType> blackPawn = mock(ChessPiece.class);
        when(blackPawn.pieceType()).thenReturn(StandardPieceType.PAWN);
        when(blackPawn.isWhite()).thenReturn(false);

        // Black pawn moves from (3,7) to (3,5) (double move).
        Move<StandardPieceType> lastMove = Move.of(blackPawn, Square.of(3, 7), Square.of(3, 5));
        when(moveHistory.getLastMove()).thenReturn(lastMove);

        // En passant target is calculated as:
        // enPassantRow = lastMove.to().y() + 1 = 5 + 1 = 6.
        // So, enPassantTarget = Square.of(lastMove.to().x(), 6) = Square.of(3, 6).
        // White pawn must be adjacent to this target.

        // Place a White pawn at the left adjacent square: (2,6)
        ChessPiece<StandardPieceType> whitePawn = mock(ChessPiece.class);
        when(whitePawn.pieceType()).thenReturn(StandardPieceType.PAWN);
        when(whitePawn.isWhite()).thenReturn(true);

        when(gameState.getPieceAt(Square.of(2, 6))).thenReturn(whitePawn);
        when(gameState.getPieceAt(Square.of(4, 6))).thenReturn(null);

        // Generate en passant moves.
        MoveCollection<StandardPieceType> moves = tested.generateMoves(gameState);

        // Expect one en passant move, with target square (3,6).
        assertEquals(1, moves.size(), "There should be one en passant move for White.");
        assertEquals(Square.of(3, 6), moves.getFirst().to());
    }


    @Test
    @SuppressWarnings("unchecked")
    public void givenValidEnPassantForBlack_whenGenerateMoves_thenMoveIsGenerated() {
        when(moveHistory.getMoveCount()).thenReturn(1);

        ChessPiece<StandardPieceType> whitePawn = mock(ChessPiece.class);
        when(whitePawn.pieceType()).thenReturn(StandardPieceType.PAWN);
        when(whitePawn.isWhite()).thenReturn(true);

        // White pawn double move: from (3,2) to (3,4)
        Move<StandardPieceType> lastMove = Move.of(whitePawn, Square.of(3, 2), Square.of(3, 4));
        when(moveHistory.getLastMove()).thenReturn(lastMove);

        ChessPiece<StandardPieceType> blackPawn = mock(ChessPiece.class);
        when(blackPawn.pieceType()).thenReturn(StandardPieceType.PAWN);
        when(blackPawn.isWhite()).thenReturn(false);

        // Adjusted: Black pawn must be at (2,3), not (2,4)
        when(gameState.getPieceAt(Square.of(2, 3))).thenReturn(blackPawn);
        when(gameState.getPieceAt(Square.of(4, 3))).thenReturn(null);

        MoveCollection<StandardPieceType> moves = tested.generateMoves(gameState);

        assertEquals(1, moves.size(), "There should be one en passant move for Black.");
        assertEquals(Square.of(3, 3), moves.getFirst().to());
    }


    @Test
    @SuppressWarnings("unchecked")
    public void givenNoAdjacentPawns_whenGenerateMoves_thenNoEnPassant() {
        when(moveHistory.getMoveCount()).thenReturn(1);

        ChessPiece<StandardPieceType> whitePawn = mock(ChessPiece.class);
        when(whitePawn.pieceType()).thenReturn(StandardPieceType.PAWN);
        when(whitePawn.isWhite()).thenReturn(true);

        Move<StandardPieceType> lastMove = Move.of(whitePawn, Square.of(4, 2), Square.of(4, 4)); // White pawn double move
        when(moveHistory.getLastMove()).thenReturn(lastMove);

        when(gameState.getPieceAt(Square.of(3, 4))).thenReturn(null); // No adjacent pawn on left
        when(gameState.getPieceAt(Square.of(5, 4))).thenReturn(null); // No adjacent pawn on right

        MoveCollection<StandardPieceType> moves = tested.generateMoves(gameState);

        assertTrue(moves.isEmpty(), "En passant should not be possible if there are no adjacent enemy pawns.");
    }
}
