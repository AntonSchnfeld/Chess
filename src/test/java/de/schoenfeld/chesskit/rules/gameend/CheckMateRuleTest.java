package de.schoenfeld.chesskit.rules.gameend;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.board.MapChessBoard;
import de.schoenfeld.chesskit.events.GameConclusion;
import de.schoenfeld.chesskit.model.*;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.MoveGenerator;
import de.schoenfeld.chesskit.rules.Rules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CheckMateRuleTest {
    private CheckMateRule tested;
    private MoveGenerator<StandardPieceType> moveGenerator;
    private GameState<StandardPieceType> gameState;
    private ChessPiece<StandardPieceType> king;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        moveGenerator = mock(MoveGenerator.class);  // Make sure this line is present
        tested = new CheckMateRule(moveGenerator);

        ChessBoard<StandardPieceType> board = new MapChessBoard<>(new ChessBoardBounds(8, 8));
        gameState = new GameState<>(board, Rules.standard());
        king = new ChessPiece<>(StandardPieceType.KING, true);
    }


    @Test
    @SuppressWarnings("unchecked")
    public void givenNoLegalMovesAndKingAttacked_whenDetectGameEndCause_thenReturnCheckmate() {
        gameState.getChessBoard().setPieceAt(Square.h5, king);

        // Mock opponent's move attacking the king
        MoveLookup<StandardPieceType> opponentMoves = new MoveLookup<>();
        opponentMoves.add(Move.claim(mock(ChessPiece.class), Square.a1, Square.h5));
        when(moveGenerator.generateMoves(any())).then(invocation -> {
            GameState<StandardPieceType> state = invocation.getArgument(0);
            if (state.isWhiteTurn()) return new MoveLookup<>();
            else return opponentMoves;
        });

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isPresent());
        assertEquals(GameConclusion.Winner.BLACK, result.get().winner());
        assertEquals("Checkmate", result.get().description());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenNoLegalMovesAndKingNotAttacked_whenDetectGameEndCause_thenReturnEmpty() {
        gameState.getChessBoard().setPieceAt(Square.h5, king);
        when(moveGenerator.generateMoves(gameState)).thenReturn(new MoveLookup<>());

        // No opponent move attacks the king
        MoveLookup<StandardPieceType> opponentMoves = mock(MoveLookup.class);
        when(opponentMoves.containsMoveTo(Square.h5)).thenReturn(false);
        when(moveGenerator.generateMoves(any())).thenReturn(opponentMoves);

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenLegalMoves_whenDetectGameEndCause_thenReturnEmpty() {
        when(moveGenerator.generateMoves(gameState))
                .thenReturn(MoveLookup.of(Move.claim(king, Square.h5, Square.g5)));

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenNoKingsOnBoard_whenDetectGameEndCause_thenReturnEmpty() {
        when(moveGenerator.generateMoves(gameState)).thenReturn(new MoveLookup<>());

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenWhiteKingOnA8AndMated_whenDetectGameEndCause_thenReturnCheckmate() {
        ChessPiece<StandardPieceType> checkingPiece = new ChessPiece<>(StandardPieceType.ROOK, false);
        gameState.getChessBoard().setPieceAt(Square.a8, king);
        gameState.getChessBoard().setPieceAt(Square.a1, checkingPiece);

        MoveLookup<StandardPieceType> enemyMoves = MoveLookup.of(
                Move.claim(checkingPiece, Square.a1, Square.a8),
                Move.claim(checkingPiece, Square.a1, Square.a7)
        );

        when(moveGenerator.generateMoves(gameState)).then(invocation -> {
            GameState<StandardPieceType> state = invocation.getArgument(0);
            if (state.isWhiteTurn()) return new MoveLookup<>();
            else return enemyMoves;
        });

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isPresent());
        assertEquals(GameConclusion.Winner.BLACK, result.get().winner());
        assertEquals("Checkmate", result.get().description());
    }

    @Test
    public void givenKingEscapesCheck_whenDetectGameEndCause_thenReturnEmpty() {
        gameState.getChessBoard().setPieceAt(Square.e1, king);
        Move<StandardPieceType> escapingMove = Move.claim(king, Square.e1, Square.d1);

        when(moveGenerator.generateMoves(gameState)).thenReturn(MoveLookup.of(escapingMove));

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenBlockedCheckmate_whenDetectGameEndCause_thenReturnEmpty() {
        ChessPiece<StandardPieceType> blockingPiece = new ChessPiece<>(StandardPieceType.PAWN, true);
        ChessPiece<StandardPieceType> checkingPiece = new ChessPiece<>(StandardPieceType.ROOK, false);
        gameState.getChessBoard().setPieceAt(Square.e1, king);
        gameState.getChessBoard().setPieceAt(Square.e8, checkingPiece);
        gameState.getChessBoard().setPieceAt(Square.e4, blockingPiece);

        Move<StandardPieceType> blockMove = Move.claim(blockingPiece, Square.e4, Square.e5);
        when(moveGenerator.generateMoves(gameState)).thenReturn(MoveLookup.of(blockMove));

        Optional<GameConclusion> result = tested.detectGameEndCause(gameState);

        assertTrue(result.isEmpty());
    }
}
