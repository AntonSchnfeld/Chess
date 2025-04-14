package de.schoenfeld.chesskit.rules.gameend;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.board.Square8x8ChessBoardBounds;
import de.schoenfeld.chesskit.board.MapChessBoard;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.events.GameConclusion;
import de.schoenfeld.chesskit.model.*;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.MoveGenerator;
import de.schoenfeld.chesskit.rules.Rules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CheckMateDetectorTest {
    private CheckMateDetector<Square8x8, StandardPieceType> tested;
    private MoveGenerator<Square8x8, StandardPieceType> moveGenerator;
    private GameState<Square8x8, StandardPieceType> gameState;
    private ChessPiece<StandardPieceType> king;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        moveGenerator = mock(MoveGenerator.class);  // Make sure this line is present
        tested = CheckMateDetector.standard();

        ChessBoard<Square8x8, StandardPieceType> board = new MapChessBoard<>(new Square8x8ChessBoardBounds());
        gameState = new GameState<>(board, Rules.standard());
        king = new ChessPiece<>(StandardPieceType.KING, Color.WHITE);
    }


    @Test
    @SuppressWarnings("unchecked")
    public void givenNoLegalMovesAndKingAttacked_whenDetectGameEndCause_thenReturnCheckmate() {
        gameState.getChessBoard().setPieceAt(Square8x8.of(7, 4), king);

        // Mock opponent's move attacking the king
        MoveLookup<Square8x8, StandardPieceType> opponentMoves = new MoveLookup<>();
        opponentMoves.add(Move.of(mock(ChessPiece.class), Square8x8.of(0, 0), Square8x8.of(7, 4)));
        when(moveGenerator.generateMoves(any())).then(invocation -> {
            GameState<Square8x8, StandardPieceType> state = invocation.getArgument(0);
            if (state.getColor() == Color.WHITE) return new MoveLookup<>();
            else return opponentMoves;
        });

        GameConclusion result = tested.detectConclusion(gameState);

        assertNotNull(result);
        assertEquals(GameConclusion.Winner.BLACK, result.winner());
        assertEquals("Checkmate", result.description());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenNoLegalMovesAndKingNotAttacked_whenDetectGameEndCause_thenReturnEmpty() {
        gameState.getChessBoard().setPieceAt(Square8x8.of(7, 4), king);
        when(moveGenerator.generateMoves(gameState)).thenReturn(new MoveLookup<>());

        // No opponent move attacks the king
        MoveLookup<Square8x8, StandardPieceType> opponentMoves = mock(MoveLookup.class);
        when(opponentMoves.containsMoveTo(Square8x8.of(7, 4))).thenReturn(false);
        when(moveGenerator.generateMoves(any())).thenReturn(opponentMoves);

        GameConclusion result = tested.detectConclusion(gameState);

        assertNull(result);
    }

    @Test
    public void givenLegalMoves_whenDetectGameEndCause_thenReturnEmpty() {
        when(moveGenerator.generateMoves(gameState))
                .thenReturn(MoveLookup.of(Move.of(king, Square8x8.of(7, 4), Square8x8.of(6, 4))));

        GameConclusion result = tested.detectConclusion(gameState);

        assertNull(result);
    }

    @Test
    public void givenNoKingsOnBoard_whenDetectGameEndCause_thenReturnEmpty() {
        when(moveGenerator.generateMoves(gameState)).thenReturn(new MoveLookup<>());

        GameConclusion result = tested.detectConclusion(gameState);

        assertNull(result);
    }

    @Test
    public void givenWhiteKingOnA8AndMated_whenDetectGameEndCause_thenReturnCheckmate() {
        ChessPiece<StandardPieceType> checkingPiece = new ChessPiece<>(StandardPieceType.ROOK, Color.BLACK);
        gameState.getChessBoard().setPieceAt(Square8x8.of(0, 8), king);
        gameState.getChessBoard().setPieceAt(Square8x8.of(0, 0), checkingPiece);

        MoveLookup<Square8x8, StandardPieceType> enemyMoves = MoveLookup.of(
                Move.of(checkingPiece, Square8x8.of(0, 0), Square8x8.of(0, 7)),
                Move.of(checkingPiece, Square8x8.of(0, 0), Square8x8.of(0, 6))
        );

        when(moveGenerator.generateMoves(gameState)).then(invocation -> {
            GameState<Square8x8, StandardPieceType> state = invocation.getArgument(0);
            if (state.getColor() == Color.WHITE) return new MoveLookup<>();
            else return enemyMoves;
        });

        GameConclusion result = tested.detectConclusion(gameState);

        assertNotNull(result);
        assertEquals(GameConclusion.Winner.BLACK, result.winner());
        assertEquals("Checkmate", result.description());
    }

    @Test
    public void givenKingEscapesCheck_whenDetectGameEndCause_thenReturnEmpty() {
        gameState.getChessBoard().setPieceAt(Square8x8.of(4, 0), king);
        Move<Square8x8, StandardPieceType> escapingMove = Move.of(king, Square8x8.of(4, 0), Square8x8.of(3, 0));

        when(moveGenerator.generateMoves(gameState)).thenReturn(MoveLookup.of(escapingMove));

        GameConclusion result = tested.detectConclusion(gameState);

        assertNull(result);
    }

    @Test
    public void givenBlockedCheckmate_whenDetectGameEndCause_thenReturnEmpty() {
        ChessPiece<StandardPieceType> blockingPiece = new ChessPiece<>(StandardPieceType.PAWN, Color.WHITE);
        ChessPiece<StandardPieceType> checkingPiece = new ChessPiece<>(StandardPieceType.ROOK, Color.BLACK);
        gameState.getChessBoard().setPieceAt(Square8x8.of(4, 0), king);
        gameState.getChessBoard().setPieceAt(Square8x8.of(4, 7), checkingPiece);
        gameState.getChessBoard().setPieceAt(Square8x8.of(4, 3), blockingPiece);

        Move<Square8x8, StandardPieceType> blockMove = Move.of(blockingPiece, Square8x8.of(4, 3), Square8x8.of(4, 4));
        when(moveGenerator.generateMoves(gameState)).thenReturn(MoveLookup.of(blockMove));

        GameConclusion result = tested.detectConclusion(gameState);

        assertNull(result);
    }
}
