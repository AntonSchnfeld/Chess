package de.schoenfeld.chess.rules;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.MoveHistory;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.rules.generative.EnPassantRule;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnPassantRuleTest {
    private EnPassantRule tested;
    private GameState<StandardPieceType> gameState;

    @BeforeEach
    public void setup() {
        tested = new EnPassantRule();
        gameState = mock(GameState.class);
        MoveHistory moveHistory = mock(MoveHistory.class);
        ChessBoard<StandardPieceType> chessBoard = mock(ChessBoard.class);

        when(gameState.chessBoard()).thenReturn(chessBoard);
        when(gameState.moveHistory()).thenReturn(moveHistory);
        when(gameState.isWhiteTurn()).thenCallRealMethod();
        when(gameState.withChessBoard(any())).thenCallRealMethod();
        when(gameState.withMoveHistory(any())).thenCallRealMethod();
    }
}
