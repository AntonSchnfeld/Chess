package de.schoenfeld.chess.events;

import java.util.UUID;

public record ErrorEvent(
        UUID gameId,
        String errorMessage
) implements GameEvent {
}
