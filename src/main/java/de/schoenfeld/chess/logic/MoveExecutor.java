package de.schoenfeld.chess.logic;

import de.schoenfeld.chess.data.ChessBoard;
import de.schoenfeld.chess.data.GameState;
import de.schoenfeld.chess.data.move.Move;
import de.schoenfeld.chess.data.move.MoveComponent;
import de.schoenfeld.chess.logic.componenthandler.MoveComponentHandler;

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

    public void executeMove(Move move, GameState gameState) {
        gameState.getMoveHistory().recordMove(move);
        gameState.setWhiteTurn(!gameState.isWhiteTurn());
        // Handles default move
        ChessBoard chessBoard = gameState.getChessBoard();
        chessBoard.removePieceAt(move.from());
        chessBoard.setPiece(move.movedPiece(), move.to());

        for (MoveComponent moveComponent : move.getComponents()) {
            // Overrides default move if necessary
            executeComponent(gameState, move, moveComponent);
        }
    }

    private <T extends MoveComponent> void executeComponent(GameState gameState, Move move, T moveComponent) {
        @SuppressWarnings("unchecked")
        MoveComponentHandler<T> handler = (MoveComponentHandler<T>) moveComponentHandlers.get(moveComponent.getClass());

        if (handler != null) {
            handler.handle(gameState, moveComponent, move);
        }
    }
}
