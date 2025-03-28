package de.schoenfeld.chesskit.events;

import de.schoenfeld.chesskit.model.PlayerData;

import java.util.UUID;

public record ErrorEvent(
        UUID gameId,
        PlayerData player,
        String errorMessage
) implements GameEvent {
}
