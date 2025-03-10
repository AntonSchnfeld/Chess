package de.schoenfeld.chess.logic.piece.strategy;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;
import de.schoenfeld.chess.move.strategy.KnightMoveStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class KnightMoveStrategyTest {
    private ChessPiece knight;
    private Position knightPosition;
    private ImmutableChessBoard chessBoard;
    private KnightMoveStrategy tested;

    @BeforeEach
    public void setup() {
        chessBoard = mock(ImmutableChessBoard.class);
        knight = mock(ChessPiece.class);
        knightPosition = new Position(3, 3);
        tested = new KnightMoveStrategy();
        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);

        when(knight.isWhite()).thenReturn(true);
        when(knight.getPieceType()).thenReturn(PieceType.KNIGHT);

        when(chessBoard.getPieceAt(knightPosition)).thenReturn(knight);
        when(chessBoard.getPiecePosition(knight)).thenReturn(knightPosition);
        when(chessBoard.getBounds()).thenReturn(bounds);
    }

    @Test
    public void givenEmptyChessBoard_whenGetPseudoLegalMoves_thenCanMoveInAllDirections() {
        // Given
        // No configuration needed for empty board

        // When
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, knightPosition);

        // Then
        List<Position> expectedPositions = List.of(
                new Position(5, 4), new Position(5, 2),
                new Position(4, 5), new Position(2, 5),
                new Position(2, 1), new Position(4, 1),
                new Position(1, 2), new Position(1, 4)
        );

        Assertions.assertEquals(expectedPositions.size(), moves.size());
        for (Position expectedPosition : expectedPositions) {
            Assertions.assertTrue(moves.containsMoveTo(expectedPosition));
            Assertions.assertTrue(moves.getMoveTo(expectedPosition).getComponents().isEmpty());
        }
    }

    @Test
    public void givenSurroundedByHostiles_whenGetPseudoLegalMoves_thenCanTakeOnAllDirections() {
        // Given
        List<Position> hostilePositions = List.of(
                new Position(5, 4), new Position(5, 2),
                new Position(4, 5), new Position(2, 5),
                new Position(2, 1), new Position(4, 1),
                new Position(1, 2), new Position(1, 4)
        );

        ChessPiece mockHostile = mock(ChessPiece.class);
        when(mockHostile.isWhite()).thenReturn(false);

        for (Position position : hostilePositions) {
            when(chessBoard.getPieceAt(position)).thenReturn(mockHostile);
        }

        // When
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, knightPosition);

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
        knightPosition = new Position(0, 0);

        when(chessBoard.getBounds()).thenReturn(bounds);
        when(chessBoard.getPieceAt(any())).thenReturn(null);
        when(chessBoard.getPieceAt(knightPosition)).thenReturn(knight);

        // When
        MoveCollection moves = tested.getPseudoLegalMoves(chessBoard, knightPosition);

        // Then
        Assertions.assertTrue(moves.isEmpty());
    }
}
