package de.schoenfeld.chess.events;

import java.util.UUID;

public record GameStartedEvent(
        UUID gameId
) implements GameEvent {}
