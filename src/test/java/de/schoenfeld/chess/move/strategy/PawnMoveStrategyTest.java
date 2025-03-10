package de.schoenfeld.chess.move.strategy;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PawnMoveStrategyTest {
    private PawnMoveStrategy tested;
    private ImmutableChessBoard chessBoard;
    private Position pawnPosition;
    private ChessPiece pawn;

    @BeforeEach
    public void setup() {
        tested = new PawnMoveStrategy();
        chessBoard = mock(ImmutableChessBoard.class);
        pawn = mock(ChessPiece.class);
        pawnPosition = new Position(3, 0);
        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);

        when(pawn.getPieceType()).thenReturn(PieceType.PAWN);
        when(pawn.isWhite()).thenReturn(true);

        when(chessBoard.getPieceAt(pawnPosition)).thenReturn(pawn);
        when(chessBoard.getBounds()).thenReturn(bounds);
        when(chessBoard.getPiecePosition(pawn)).thenReturn(pawnPosition);
    }

    @Test
    public void givenEmptyBoard_whenGetPseudoLegalMoves_thenCanAdvance() {
        // Given
        // No configuration needed for empty board

        // When
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, pawnPosition);

        // Then
        Assertions.assertEquals(2, moves.size());
        Assertions.assertTrue(moves.containsMoveTo(pawnPosition.offset(0, 1)));
        Assertions.assertTrue(moves.containsMoveTo(pawnPosition.offset(0, 2)));
    }

    @Test
    public void givenMovementBlocked_whenGetPseudoLegalMoves_thenCannotAdvance() {
        // Given
        ChessPiece blockingPiece = mock(ChessPiece.class);
        Position blockingPiecePosition = pawnPosition.offset(0, 1);

        when(chessBoard.getPieceAt(blockingPiecePosition)).thenReturn(blockingPiece);

        // When
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, pawnPosition);

        // Then
        Assertions.assertTrue(moves.isEmpty());
    }

    @Test
    public void givenHostilesOnDiagonals_whenGetPseudoLegalMoves_thenCannotAdvance() {
        // Given
        ChessPiece mockLeftHostile = mock(ChessPiece.class);
        ChessPiece mockRightHostile = mock(ChessPiece.class);
        Position leftHostilePosition = pawnPosition.offset(-1, 1);
        Position rightHostilePosition = pawnPosition.offset(1, 1);

        when(mockLeftHostile.isWhite()).thenReturn(false);
        when(mockRightHostile.isWhite()).thenReturn(false);

        when(chessBoard.getPieceAt(leftHostilePosition)).thenReturn(mockLeftHostile);
        when(chessBoard.getPieceAt(rightHostilePosition)).thenReturn(mockRightHostile);

        // When
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, pawnPosition);

        // Then
        Assertions.assertEquals(4, moves.size());
        Assertions.assertTrue(moves.containsMoveTo(leftHostilePosition));
        Assertions.assertTrue(moves.getMoveTo(leftHostilePosition).hasComponent(CaptureComponent.class));
        Assertions.assertEquals(moves.getMoveTo(leftHostilePosition).getComponent(CaptureComponent.class).capturedPiece(), mockLeftHostile);
        Assertions.assertTrue(moves.containsMoveTo(rightHostilePosition));
        Assertions.assertTrue(moves.getMoveTo(rightHostilePosition).hasComponent(CaptureComponent.class));
        Assertions.assertEquals(moves.getMoveTo(rightHostilePosition).getComponent(CaptureComponent.class).capturedPiece(), mockRightHostile);
        Assertions.assertTrue(moves.containsMoveTo(pawnPosition.offset(0, 1)));
        Assertions.assertTrue(moves.containsMoveTo(pawnPosition.offset(0, 2)));
    }

    @Test
    public void givenFriendliesOnDiagonals_whenGetPseudoLegalMoves_thenCannotCapture() {
        // Given
        ChessPiece mockLeftFriendly = mock(ChessPiece.class);
        ChessPiece mockRightFriendly = mock(ChessPiece.class);
        Position leftFriendlyPosition = pawnPosition.offset(-1, 1);
        Position rightFriendlyPosition = pawnPosition.offset(1, 1);

        when(mockLeftFriendly.isWhite()).thenReturn(true);
        when(mockRightFriendly.isWhite()).thenReturn(true);

        when(chessBoard.getPieceAt(leftFriendlyPosition)).thenReturn(mockLeftFriendly);
        when(chessBoard.getPieceAt(rightFriendlyPosition)).thenReturn(mockRightFriendly);

        // When
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, pawnPosition);

        // Then
        Assertions.assertEquals(2, moves.size());
        Assertions.assertTrue(moves.containsMoveTo(pawnPosition.offset(0, 1)));
        Assertions.assertTrue(moves.containsMoveTo(pawnPosition.offset(0, 2)));
    }

    @Test
    public void givenTinyBoard_whenGetPseudoLegalMoves_thenCannotMove() {
        // Given
        ChessBoardBounds tinyBounds = new ChessBoardBounds(1, 1);
        when(chessBoard.getBounds()).thenReturn(tinyBounds);

        // When
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, pawnPosition);

        // Then
        Assertions.assertTrue(moves.isEmpty());
    }

    @Test
    public void givenPawnHasMoved_whenGetValidMoves_thenCanOnlyAdvance() {
        // Given
        when(pawn.hasMoved()).thenReturn(false);

        // When
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, pawnPosition);

        // Then
        Assertions.assertTrue(moves.containsMoveTo(pawnPosition.offset(0, 1)));
    }
}
