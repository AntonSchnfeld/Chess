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

import java.util.List;

import static org.mockito.Mockito.*;

public class KingMoveStrategyTest {
    private ImmutableChessBoard chessBoard;
    private ChessPiece king;
    private KingMoveStrategy tested;
    private Position kingPosition;

    @BeforeEach
    public void setup() {
        chessBoard = mock(ImmutableChessBoard.class);
        king = mock(ChessPiece.class);
        tested = new KingMoveStrategy();
        kingPosition = new Position(3, 3);

        ChessBoardBounds chessBoardBounds = new ChessBoardBounds(8, 8);

        when(king.isWhite()).thenReturn(true);
        when(king.getPieceType()).thenReturn(PieceType.KING);

        when(chessBoard.getBounds()).thenReturn(chessBoardBounds);
        when(chessBoard.getPieceAt(kingPosition)).thenReturn(king);
        when(chessBoard.getPiecePosition(king)).thenReturn(kingPosition);
    }

    @Test
    public void givenEmptyBoard_whenGetPseudoLegalMoves_thenCanMoveInAllDirections() {
        // Given
        // No configuration needed for empty board

        // When
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, kingPosition);

        // Then
        List<Position> expectedPositions = List.of(
                new Position(2, 4), new Position(3, 4), new Position(4, 4),
                new Position(2, 2), new Position(3, 2), new Position(4, 2),
                new Position(2, 3), new Position(4, 3)
        );

        Assertions.assertEquals(expectedPositions.size(), moves.size());
        for (Position expectedPosition : expectedPositions) {
            Assertions.assertTrue(moves.containsMoveTo(expectedPosition));
            Assertions.assertTrue(moves.getMoveTo(expectedPosition).getComponents().isEmpty());
        }
    }

    @Test
    public void givenSurroundedByHostiles_whenGetPseudoLegalMoves_thenCanCaptureInAllDirections() {
        // Given
        ChessPiece mockHostile = mock(ChessPiece.class);

        when(mockHostile.isWhite()).thenReturn(false);

        List<Position> hostilePositions = List.of(
                new Position(2, 4), new Position(3, 4), new Position(4, 4),
                new Position(2, 2), new Position(3, 2), new Position(4, 2),
                new Position(2, 3), new Position(4, 3)
        );

        for (Position position : hostilePositions) {
            when(chessBoard.getPieceAt(position)).thenReturn(mockHostile);
        }

        // When
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, kingPosition);

        // Then
        Assertions.assertEquals(hostilePositions.size(), moves.size());
        for (Position position : hostilePositions) {
            Assertions.assertTrue(moves.containsMoveTo(position));
            Assertions.assertTrue(moves.getMoveTo(position).isCapture());
            Assertions.assertEquals(mockHostile, moves.getMoveTo(position).getComponent(CaptureComponent.class).capturedPiece());
        }
    }

    @Test
    public void givenTinyBoard_whenGetPseudoLegalMoves_thenCannotMove() {
        // Given
        ChessBoardBounds bounds = new ChessBoardBounds(1, 1);
        kingPosition = new Position(0, 0);

        when(chessBoard.getBounds()).thenReturn(bounds);
        when(chessBoard.getPieceAt(any())).thenReturn(null);
        when(chessBoard.getPieceAt(kingPosition)).thenReturn(king);

        // When
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, kingPosition);

        // Then
        Assertions.assertTrue(moves.isEmpty());
    }
}
