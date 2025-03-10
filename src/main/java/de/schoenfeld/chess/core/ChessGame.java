package de.schoenfeld.chess.core;

import de.schoenfeld.chess.events.EventBus;
import de.schoenfeld.chess.events.MoveProposedEvent;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.MoveExecutor;
import de.schoenfeld.chess.move.MoveGenerator;

import java.util.UUID;

public class ChessGame {
    private final MoveGenerator moveGenerator;
    private final EventBus eventBus;
    private final MoveExecutor moveExecutor;
    private final UUID gameId;
    private GameState gameState;

    public ChessGame(GameState gameState, MoveGenerator moveGenerator, EventBus eventBus) {
        this(UUID.randomUUID(), gameState, moveGenerator, eventBus);
    }

    public ChessGame(UUID gameId, GameState gameState, MoveGenerator moveGenerator, EventBus eventBus) {
        this.gameState = gameState;
        this.moveGenerator = moveGenerator;
        this.eventBus = eventBus;
        this.moveExecutor = new MoveExecutor();
        this.gameId = gameId;

        // Register for move proposals
        eventBus.subscribe(MoveProposedEvent.class, this::handleMoveProposed);
    }

    public ChessGame() {
        this(new GameState(), new MoveGenerator(), new EventBus());
    }

    private void handleMoveProposed(MoveProposedEvent event) {
        if (!event.gameId().equals(gameId)) return;

        if (event.player().isWhite() != gameState.isWhiteTurn()) {
            // TODO: Report error
            return;
        }
        MoveCollection currentValidMoves = moveGenerator.generateMoves(gameState);
        if (!currentValidMoves.contains(event.move())) {
            // TODO: Report error
        }

        gameState = moveExecutor.executeMove(event.move(), gameState);

        // TODO: Send some kinda event to subscribers trololol
    }

    public GameState getGameState() {
        return gameState;
    }

    public UUID getGameId() {
        return gameId;
    }

    public MoveGenerator getMoveGenerator() {
        return moveGenerator;
    }
}