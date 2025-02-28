package de.schoenfeld.chess.logic;

import de.schoenfeld.chess.data.Position;
import de.schoenfeld.chess.data.ReadOnlyChessBoard;
import de.schoenfeld.chess.data.ReadOnlyGameState;
import de.schoenfeld.chess.data.move.Move;
import de.schoenfeld.chess.logic.piece.ChessPiece;
import de.schoenfeld.chess.logic.piece.PieceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class CheckDetectorTest {
    private ReadOnlyChessBoard mockChessBoard;
    private ReadOnlyGameState mockGameState;
    private CheckDetector checkDetector;
    private ChessPiece mockWhiteKing, mockBlackKing;
    private Position whiteKingPos, blackKingPos;

    @BeforeEach
    public void setup() {
        mockChessBoard = mock(ReadOnlyChessBoard.class);
        mockGameState = mock(ReadOnlyGameState.class);
        mockWhiteKing = mock(ChessPiece.class);
        mockBlackKing = mock(ChessPiece.class);
        when(mockWhiteKing.getPieceType()).thenReturn(PieceType.KING);
        when(mockBlackKing.getPieceType()).thenReturn(PieceType.KING);
        when(mockWhiteKing.isWhite()).thenReturn(true);
        when(mockBlackKing.isWhite()).thenReturn(false);

        when(mockGameState.getChessBoard()).thenReturn(mockChessBoard);
        when(mockGameState.isWhiteTurn()).thenReturn(true);
        when(mockChessBoard.getPiecesOfType(PieceType.KING, true)).thenReturn(List.of(mockWhiteKing));
        when(mockChessBoard.getPiecesOfType(PieceType.KING, false)).thenReturn(List.of(mockBlackKing));
        when(mockChessBoard.getPiecePosition(mockWhiteKing)).thenReturn(whiteKingPos);
        when(mockChessBoard.getPiecePosition(mockBlackKing)).thenReturn(blackKingPos);

        whiteKingPos = new Position(0, 0);
        blackKingPos = new Position(7, 7);

        checkDetector = new CheckDetector();
    }

    @Test
    public void givenOnlyBlackAndWhiteKing_whenIsCheck_thenReturnFalse() {
        Move move = Move.of(mockWhiteKing, whiteKingPos, whiteKingPos.offset(1, 1));
        Position to = new Position(1, 1);

        boolean value = checkDetector.isCheck(mockGameState, move);

        Assertions.assertFalse(value);
    }
}
