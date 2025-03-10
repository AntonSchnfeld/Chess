package de.schoenfeld.chess.events;

import de.schoenfeld.chess.model.GameState;

import java.util.UUID;

public record GameStateChangedEvent(
        UUID gameId,
        GameState newState
) implements GameEvent {
}