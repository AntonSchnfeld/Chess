package de.schoenfeld.chesskit.events;

import java.util.UUID;

public record GameEndedEvent(
        UUID gameId,
        GameConclusion cause
) implements GameEvent {
}
