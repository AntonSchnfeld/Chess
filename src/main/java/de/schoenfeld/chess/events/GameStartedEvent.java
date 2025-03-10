package de.schoenfeld.chess.events;

import de.schoenfeld.chess.model.PlayerData;

import java.util.UUID;

public record GameStartedEvent(
        UUID gameId,
        PlayerData whitePlayer,
        PlayerData blackPlayer
) implements GameEvent {
}
