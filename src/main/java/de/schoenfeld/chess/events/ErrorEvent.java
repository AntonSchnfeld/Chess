package de.schoenfeld.chess.events;

import de.schoenfeld.chess.model.PlayerData;

import java.util.UUID;

public record ErrorEvent(
        UUID gameId,
        PlayerData player,
        String errorMessage
) implements GameEvent {
}
