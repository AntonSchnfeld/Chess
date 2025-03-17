package de.schoenfeld.chess.events;

import java.util.UUID;

public record GameEndedEvent(
        UUID gameId,
        GameConclusion cause
) implements GameEvent {
}
