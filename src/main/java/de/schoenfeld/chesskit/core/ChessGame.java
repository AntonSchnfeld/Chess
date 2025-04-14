package de.schoenfeld.chesskit.core;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.events.*;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.Rules;

import java.util.UUID;

public class ChessGame<T extends Tile, P extends PieceType> {

    private final EventBus eventBus;
    private final UUID gameId;
    private final Rules<T, P> rules;
    private final GameState<T, P> gameState;

    public ChessGame(GameState<T, P> gameState, Rules<T, P> rules, EventBus eventBus) {
        this(UUID.randomUUID(), gameState, rules, eventBus);
    }

    public ChessGame(UUID gameId, GameState<T, P> gameState, Rules<T, P> rules, EventBus eventBus) {
        this.gameState = gameState;
        this.rules = rules;
        this.eventBus = eventBus;
        this.gameId = gameId;

        eventBus.subscribe(MoveProposedEvent.class, this::handleMoveProposed);
    }

    public void start() {
        eventBus.publish(new GameStartedEvent(gameId));

        var cause = rules.detectConclusion(gameState);

        if (cause != null) {
            eventBus.publish(new GameEndedEvent(gameId, cause));
            eventBus.publish(new GameStateChangedEvent<>(gameId, gameState));
        }

        eventBus.publish(new GameStateChangedEvent<>(gameId, gameState));
    }

    private void handleMoveProposed(MoveProposedEvent<T, P> event) {
        if (!event.gameId().equals(gameId)) {
            return;
        }

        if (event.player().color() != gameState.getColor()) {
            eventBus.publish(new ErrorEvent(gameId, event.player(), "Not your turn"));
            return;
        }

        if (event.move().movedPiece().color() != event.player().color()) {
            eventBus.publish(new ErrorEvent(gameId, event.player(), "Not your piece"));
            return;
        }

        MoveLookup<T, P> currentValidMoves = rules.generateMoves(gameState);

        if (!currentValidMoves.contains(event.move())) {
            eventBus.publish(new ErrorEvent(gameId, event.player(), "Invalid move: " + event.move()));
            return;
        }

        gameState.makeMove(event.move());
        var cause = rules.detectConclusion(gameState);
        if (cause != null) {
            eventBus.publish(new GameEndedEvent(gameId, cause));
            eventBus.publish(new GameStateChangedEvent<>(gameId, gameState));
        } else eventBus.publish(new GameStateChangedEvent<>(gameId, gameState));
    }

    public GameState<T, P> getGameState() {
        return gameState;
    }

    public UUID getGameId() {
        return gameId;
    }

    public Rules<T, P> getRules() {
        return rules;
    }
}
