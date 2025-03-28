package de.schoenfeld.chesskit.events;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;

import java.util.UUID;

public record GameStateChangedEvent<T extends PieceType>(
        UUID gameId,
        GameState<T> newState
) implements GameEvent {
}