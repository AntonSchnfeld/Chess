package de.schoenfeld.chesskit.events;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;

import java.util.UUID;

public record GameStateChangedEvent<T extends Tile, P extends PieceType>(
        UUID gameId,
        GameState<T, P> newState
) implements GameEvent {
}