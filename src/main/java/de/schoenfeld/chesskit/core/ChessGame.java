package de.schoenfeld.chesskit.core;

import de.schoenfeld.chesskit.events.*;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveCollection;
import de.schoenfeld.chesskit.rules.Rules;

import java.util.UUID;

public class ChessGame<T extends PieceType> {

    private final EventBus eventBus;
    private final UUID gameId;
    private final Rules<T> rules;
    private final GameState<T> gameState;

    public ChessGame(GameState<T> gameState, Rules<T> rules, EventBus eventBus) {
        this(UUID.randomUUID(), gameState, rules, eventBus);
    }

    public ChessGame(UUID gameId, GameState<T> gameState, Rules<T> rules, EventBus eventBus) {
        this.gameState = gameState;
        this.rules = rules;
        this.eventBus = eventBus;
        this.gameId = gameId;

        eventBus.subscribe(MoveProposedEvent.class, this::handleMoveProposed);
    }

    public void start() {
        eventBus.publish(new GameStartedEvent(gameId));

        rules.detectGameEndCause(gameState).ifPresent(gameConclusion -> {
            eventBus.publish(new GameEndedEvent(gameId, gameConclusion));
        });

        eventBus.publish(new GameStateChangedEvent<>(gameId, gameState));
    }

    private void handleMoveProposed(MoveProposedEvent<T> event) {
        if (!event.gameId().equals(gameId)) {
            return;
        }

        if (event.player().isWhite() != gameState.isWhiteTurn()) {
            eventBus.publish(new ErrorEvent(gameId, event.player(), "Not your turn"));
            return;
        }

        if (event.move().movedPiece().isWhite() != event.player().isWhite()) {
            eventBus.publish(new ErrorEvent(gameId, event.player(), "Not your piece"));
            return;
        }

        MoveCollection<T> currentValidMoves = rules.generateMoves(gameState);

        if (!currentValidMoves.contains(event.move())) {
            eventBus.publish(new ErrorEvent(gameId, event.player(), "Invalid move: " + event.move()));
            return;
        }

        event.move().executeOn(gameState);
        rules.detectGameEndCause(gameState).ifPresentOrElse(
                gameConclusion -> {
                    eventBus.publish(new GameEndedEvent(gameId, gameConclusion));
                },
                () -> {
                    eventBus.publish(new GameStateChangedEvent<>(gameId, gameState));
                }
        );
    }

    public GameState<T> getGameState() {
        return gameState;
    }

    public UUID getGameId() {
        return gameId;
    }

    public Rules<T> getRules() {
        return rules;
    }
}
