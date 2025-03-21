package de.schoenfeld.chess.core;

import de.schoenfeld.chess.events.*;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.Rules;

import java.util.Optional;
import java.util.UUID;

public class ChessGame {
    private final EventBus eventBus;
    private final UUID gameId;
    private GameState gameState;
    private final Rules rules;

    public ChessGame(GameState gameState, Rules rules, EventBus eventBus) {
        this(UUID.randomUUID(), gameState, rules, eventBus);
    }

    public ChessGame(UUID gameId, GameState gameState, Rules rules, EventBus eventBus) {
        this.gameState = gameState;
        this.rules = rules;
        this.eventBus = eventBus;
        this.gameId = gameId;

        // Register for move proposals
        eventBus.subscribe(MoveProposedEvent.class, this::handleMoveProposed);
    }

    public ChessGame() {
        this(new GameState(), Rules.DEFAULT, new EventBus());
    }

    public ChessGame(EventBus eventBus) {
        this(new GameState(), Rules.DEFAULT, eventBus);
    }

    public void start() {
        eventBus.publish(new GameStartedEvent(gameId));

        Optional<GameConclusion> gameConclusion = rules.detectGameEndCause(gameState);
        if (gameConclusion.isPresent()) {
            eventBus.publish(new GameEndedEvent(gameId, gameConclusion.get()));
            return;
        }

        eventBus.publish(new GameStateChangedEvent(gameId, gameState));
    }

    private void handleMoveProposed(MoveProposedEvent event) {
        if (!event.gameId().equals(gameId)) return;

        if (event.player().isWhite() != gameState.isWhiteTurn()) {
            eventBus.publish(new ErrorEvent(gameId, event.player(), "Not your turn"));
            return;
        }

        if (event.move().movedPiece().isWhite() != event.player().isWhite()) {
            eventBus.publish(new ErrorEvent(gameId, event.player(), "Not your piece"));
            return;
        }

        GameState moveState = event.move().executeOn(gameState);
        Optional<GameConclusion> gameEndCause = rules.detectGameEndCause(moveState);
        if (gameEndCause.isPresent()) {
            eventBus.publish(new GameEndedEvent(gameId, gameEndCause.get()));
            return;
        }

        MoveCollection currentValidMoves = rules.generateMoves(gameState);

        if (!currentValidMoves.contains(event.move())) {
            eventBus.publish(new ErrorEvent(gameId, event.player(), "Invalid move"));
            return;
        }

        gameState = event.move().executeOn(gameState);

        GameStateChangedEvent gameStateChangedEvent = new GameStateChangedEvent(gameId, gameState);
        eventBus.publish(gameStateChangedEvent);
    }

    public GameState getGameState() {
        return gameState;
    }

    public UUID getGameId() {
        return gameId;
    }

    public Rules getRules() {
        return rules;
    }
}