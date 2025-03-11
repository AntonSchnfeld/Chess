package de.schoenfeld.chess;

import de.schoenfeld.chess.events.*;
import de.schoenfeld.chess.model.PlayerData;

import java.util.UUID;

public abstract class Player {
    protected final EventBus eventBus;
    protected final PlayerData playerData;
    protected UUID gameId;

    public Player(PlayerData playerData, EventBus eventBus) {
        this.eventBus = eventBus;
        this.playerData = playerData;

        eventBus.subscribe(GameStartedEvent.class, this::onGameStarted);
        eventBus.subscribe(GameEndedEvent.class, this::onGameEnded);
        eventBus.subscribe(GameStateChangedEvent.class, this::onGameStateChanged);
        eventBus.subscribe(ErrorEvent.class, this::onError);
    }

    protected void onGameStarted(GameStartedEvent event) {
        gameId = event.gameId();
    }

    protected abstract void onGameEnded(GameEndedEvent event);

    protected abstract void onGameStateChanged(GameStateChangedEvent event);

    protected abstract void onError(ErrorEvent event);
}
