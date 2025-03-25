package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.move.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class MoveComponentTest<T extends PieceType> {
    protected GameState<T> gameState;
    protected Move<T> move;
    protected MoveComponent<T> moveComponent;
    protected Square from;
    protected Square to;
    protected ChessPiece<T> piece;

    @BeforeEach
    protected abstract void setup();

    @Test
    void givenMove_whenExecuteOn_thenComponentAppliesEffect() {
        // When
        move.executeOn(gameState);

        // Then
        verifyComponentExecuted(gameState, move);
    }

    @Test
    void givenExecutedMove_whenUndoOn_thenComponentReversesEffect() {
        // When
        move.executeOn(gameState);
        move.undoOn(gameState);

        // Then
        verifyComponentUndone(gameState, move);
    }

    protected abstract void verifyComponentExecuted(GameState<T> gameState, Move<T> move);

    protected abstract void verifyComponentUndone(GameState<T> gameState, Move<T> move);
}
