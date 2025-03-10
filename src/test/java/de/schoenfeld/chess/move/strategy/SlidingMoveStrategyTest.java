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

public class SlidingMoveStrategyTest {
    private Position position;
    private ChessPiece piece;
    private ImmutableChessBoard chessBoard;
    private SlidingMoveStrategy tested;

    @BeforeEach
    public void setup() {
        PieceType mockedType = mock(PieceType.class);
        chessBoard = mock(ImmutableChessBoard.class);
        piece = mock(ChessPiece.class);
        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);
        position = new Position(3, 3);
        tested = new SlidingMoveStrategy(SlidingMoveStrategy.ALL_DIRECTIONS);

        when(piece.getPieceType()).thenReturn(mockedType);
        when(piece.isWhite()).thenReturn(true);
        when(chessBoard.getPieceAt(position)).thenReturn(piece);
        when(chessBoard.getPiecePosition(piece)).thenReturn(position);
        when(chessBoard.getBounds()).thenReturn(bounds);
    }

    @Test
    public void givenEmptyBoard_whenGetPseudoLegalMoves_thenCanMoveInAllDirections() {
        // Given
        // No configuration needed for empty board

        // When
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, position);

        // Then
        List<Position> expectedPositions = List.of(
                // Horizontal
                new Position(4, 3), new Position(5, 3), new Position(6, 3),
                new Position(7, 3), new Position(2, 3), new Position(1, 3),
                new Position(0, 3), new Position(3, 4), new Position(3, 5),
                new Position(3, 6), new Position(3, 7), new Position(3, 2),
                new Position(3, 1), new Position(3, 0),
                // Diagonal
                new Position(4, 4), new Position(5, 5), new Position(6, 6),
                new Position(7, 7), new Position(2, 2), new Position(1, 1),
                new Position(0, 0), new Position(4, 2), new Position(5, 1),
                new Position(6, 0), new Position(2, 4), new Position(1, 5),
                new Position(0, 6)
        );

        Assertions.assertEquals(expectedPositions.size(), moves.size());
        for (Position expectedPosition : expectedPositions) {
            Assertions.assertTrue(moves.containsMoveTo(expectedPosition));
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
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, position);

        // Then
        Assertions.assertEquals(hostilePositions.size(), moves.size());
        for (Position position : hostilePositions) {
            Assertions.assertTrue(moves.containsMoveTo(position));
            Assertions.assertTrue(moves.getMoveTo(position).isCapture());
            Assertions.assertEquals(mockHostile, moves.getMoveTo(position).getComponent(CaptureComponent.class).capturedPiece());
        }
    }

    @Test
    public void givenTinyBoard_whenGetPseudoLegalMoves_thenCannotAdvance() {
        // Given
        ChessBoardBounds bounds = new ChessBoardBounds(1, 1);
        position = new Position(0, 0);

        when(chessBoard.getBounds()).thenReturn(bounds);
        when(chessBoard.getPieceAt(any())).thenReturn(null);
        when(chessBoard.getPieceAt(position)).thenReturn(piece);

        // When
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, position);

        // Then
        Assertions.assertTrue(moves.isEmpty());
    }
}