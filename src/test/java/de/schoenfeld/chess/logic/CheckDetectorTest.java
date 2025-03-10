package de.schoenfeld.chess.logic;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.strategy.MoveStrategy;
import de.schoenfeld.chess.rules.CheckDetector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class CheckDetectorTest {
    private ReadOnlyGameState mockGameState;
    private ImmutableChessBoard mockChessBoard;
    private CheckDetector checkDetector;
    private ChessPiece mockWhiteKing;
    private Position whiteKingPos;

    @BeforeEach
    public void setup() {
        mockChessBoard = mock(ImmutableChessBoard.class);
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
        when(mockChessBoard.getPieceAt(whiteKingPos)).thenReturn(mockWhiteKing);
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

    @Test
    public void givenRevealedCheck_whenIsCheck_thenReturnTrue() {
        // Arrange
        ChessPiece movedPiece = mock(ChessPiece.class);
        ChessPiece checkingPiece = mock(ChessPiece.class);
        MoveStrategy checkingPieceMoveStrategy = mock(MoveStrategy.class);

        Position movedPieceStart = new Position(1, 7);
        Position movedPieceEnd = movedPieceStart.offset(0, 1);
        Position checkingPiecePos = new Position(0, 7);

        Move revealingMove = Move.of(movedPiece, movedPieceStart, movedPieceEnd);
        Move revealedCheckMove = Move.of(checkingPiece, checkingPiecePos, whiteKingPos);

        // Mock moved piece
        when(movedPiece.isWhite()).thenReturn(false);
        when(mockChessBoard.getPieceAt(movedPieceStart)).thenReturn(movedPiece);
        when(mockChessBoard.getPiecePosition(movedPiece)).thenReturn(movedPieceStart);

        // Mock checking piece
        when(checkingPiece.isWhite()).thenReturn(false);
        when(checkingPiece.getPieceType()).thenReturn(PieceType.ROOK);  // Example: Rook delivering check
        when(checkingPiece.getPieceType().moveStrategy()).thenReturn(checkingPieceMoveStrategy);
        when(mockChessBoard.getPieceAt(checkingPiecePos)).thenReturn(checkingPiece);
        when(mockChessBoard.getPiecePosition(checkingPiece)).thenReturn(checkingPiecePos);

        // The checking piece attacks the king after movedPiece moves
        when(checkingPieceMoveStrategy.getPseudoLegalMoves(mockChessBoard, checkingPiecePos))
                .thenReturn(MoveCollection.of(revealedCheckMove));

        // Simulate board update: movedPiece is gone after moving
        when(mockChessBoard.getPieceAt(movedPieceStart)).thenReturn(null);

        // Act
        boolean isCheck = checkDetector.isCheck(mockGameState, revealingMove);

        // Assert
        Assertions.assertTrue(isCheck, "Moving the piece should reveal a check.");
    }

}
