package de.schoenfeld.chess.logic;

import de.schoenfeld.chess.data.ChessBoardBounds;
import de.schoenfeld.chess.data.Position;
import de.schoenfeld.chess.data.ReadOnlyChessBoard;
import de.schoenfeld.chess.data.ReadOnlyGameState;
import de.schoenfeld.chess.data.move.Move;
import de.schoenfeld.chess.data.move.MoveCollection;
import de.schoenfeld.chess.logic.piece.ChessPiece;
import de.schoenfeld.chess.logic.piece.PieceType;
import de.schoenfeld.chess.logic.piece.strategy.MoveStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class CheckDetectorTest {
    private ReadOnlyGameState mockGameState;
    private ReadOnlyChessBoard mockChessBoard;
    private CheckDetector checkDetector;
    private ChessPiece mockWhiteKing;
    private Position whiteKingPos;

    @BeforeEach
    public void setup() {
        mockChessBoard = mock(ReadOnlyChessBoard.class);
        mockGameState = mock(ReadOnlyGameState.class);
        mockWhiteKing = mock(ChessPiece.class);

        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);

        when(mockWhiteKing.getPieceType()).thenReturn(PieceType.KING);
        when(mockWhiteKing.isWhite()).thenReturn(true);

        whiteKingPos = new Position(0, 0);

        when(mockGameState.getChessBoard()).thenReturn(mockChessBoard);
        when(mockGameState.isWhiteTurn()).thenReturn(true);
        when(mockChessBoard.getPiecesOfType(PieceType.KING, true)).thenReturn(List.of(mockWhiteKing));
        when(mockChessBoard.getPiecePosition(mockWhiteKing)).thenReturn(whiteKingPos);
        when(mockChessBoard.getBounds()).thenReturn(bounds);

        checkDetector = new CheckDetector();
    }

    @Test
    public void givenOnlyBlackAndWhiteKing_whenIsCheck_thenReturnFalse() {
        ChessPiece mockBlackKing = mock(ChessPiece.class);
        Position blackKingPos = new Position(7, 7);

        when(mockBlackKing.getPieceType()).thenReturn(PieceType.KING);
        when(mockBlackKing.isWhite()).thenReturn(false);
        when(mockChessBoard.getPiecesOfType(PieceType.KING, false)).thenReturn(List.of(mockBlackKing));
        when(mockChessBoard.getPiecePosition(mockBlackKing)).thenReturn(blackKingPos);

        Move move = Move.of(mockWhiteKing, whiteKingPos, whiteKingPos.offset(1, 1));
        when(mockChessBoard.getPieceAt(move.to())).thenReturn(mockWhiteKing);

        boolean isCheck = checkDetector.isCheck(mockGameState, move);

        Assertions.assertFalse(isCheck);
        verify(mockChessBoard).getPiecesOfType(PieceType.KING, false);
    }

    @Test
    public void givenWhiteKingInCheck_whenIsCheck_thenReturnTrue() {
        ChessPiece mockHostile = mock(ChessPiece.class);
        PieceType mockPieceType = mock(PieceType.class);
        MoveStrategy moveStrategy = mock(MoveStrategy.class);

        Position hostilePos = new Position(7, 7);
        Position hostileToPos = new Position(0, 7);
        Move checkMove = Move.of(mockHostile, hostilePos, hostileToPos);
        Move pseudoLegalMove = Move.of(mockHostile, hostileToPos, whiteKingPos);

        when(mockHostile.getPieceType()).thenReturn(mockPieceType);
        when(mockHostile.isWhite()).thenReturn(false);
        when(mockHostile.getPieceType().moveStrategy()).thenReturn(moveStrategy);

        MoveCollection pseudoLegalMoves = MoveCollection.of(pseudoLegalMove);
        when(moveStrategy.getPseudoLegalMoves(mockChessBoard, hostileToPos)).thenReturn(pseudoLegalMoves);

        boolean isCheck = checkDetector.isCheck(mockGameState, checkMove);

        Assertions.assertTrue(isCheck);
        verify(mockChessBoard).getPiecesOfType(PieceType.KING, true);
        verify(moveStrategy).getPseudoLegalMoves(mockChessBoard, hostileToPos);
    }
}
