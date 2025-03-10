package de.schoenfeld.chess.move;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.components.MoveComponent;
import de.schoenfeld.chess.move.handlers.MoveComponentHandler;

import java.util.HashMap;
import java.util.Map;

public class MoveExecutor {
    private final Map<Class<? extends MoveComponent>, MoveComponentHandler<? extends MoveComponent>> moveComponentHandlers;

    public MoveExecutor(Map<Class<? extends MoveComponent>, MoveComponentHandler<? extends MoveComponent>> moveComponentHandlers) {
        this.moveComponentHandlers = Map.copyOf(moveComponentHandlers);
    }

    public MoveExecutor() {
        this(new HashMap<>());
    }

    public GameState executeMove(Move move, GameState gameState) {
        gameState = gameState.withMoveHistory(gameState.moveHistory()
                .withMoveRecorded(move));
        // Handles default move
        gameState = gameState.withChessBoard(gameState.chessBoard()
                .withPieceMoved(move.from(), move.to()));

        for (MoveComponent moveComponent : move.getComponents()) {
            // Overrides default move if necessary
            gameState = executeComponent(gameState, move, moveComponent);
        }

        return gameState;
    }

    private <T extends MoveComponent> GameState executeComponent(GameState gameState, Move move, T moveComponent) {
        @SuppressWarnings("unchecked")
        MoveComponentHandler<T> handler = (MoveComponentHandler<T>) moveComponentHandlers.get(moveComponent.getClass());

        if (handler != null) {
            gameState = handler.handle(gameState, moveComponent, move);
        }

        return gameState;
    }
}
