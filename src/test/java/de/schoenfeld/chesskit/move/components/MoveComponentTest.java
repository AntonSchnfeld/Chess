package de.schoenfeld.chesskit.move.components;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.move.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class MoveComponentTest<T extends Tile, P extends PieceType> {
    protected GameState<T, P> gameState;
    protected Move<T, P> move;
    protected MoveComponent<T, P> moveComponent;
    protected Square8x8 from;
    protected Square8x8 to;
    protected ChessPiece<P> piece;

    @BeforeEach
    protected abstract void setup();

    @Test
    void givenMove_whenMakeOn_thenComponentAppliesEffect() {
        // When
        gameState.makeMove(move);

        // Then
        verifyComponentExecuted(gameState, move);
    }

    @Test
    void givenExecutedMove_whenUnmakeOn_thenComponentReversesEffect() {
        // When
        gameState.makeMove(move);
        gameState.unmakeLastMove();

        // Then
        verifyComponentUndone(gameState, move);
    }

    protected abstract void verifyComponentExecuted(GameState<T, P> gameState, Move<T, P> move);

    protected abstract void verifyComponentUndone(GameState<T, P> gameState, Move<T, P> move);
}
