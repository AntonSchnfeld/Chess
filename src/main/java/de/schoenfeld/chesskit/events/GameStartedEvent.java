package de.schoenfeld.chesskit.events;

import java.util.UUID;

public record GameStartedEvent(
        UUID gameId
) implements GameEvent {
}
