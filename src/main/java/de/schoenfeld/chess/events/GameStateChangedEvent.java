package de.schoenfeld.chess.events;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;

import java.util.UUID;

public record GameStateChangedEvent<T extends PieceType>(
        UUID gameId,
        GameState<T> newState
) implements GameEvent {
}