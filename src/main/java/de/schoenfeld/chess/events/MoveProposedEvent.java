package de.schoenfeld.chess.events;

import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.PlayerData;
import de.schoenfeld.chess.move.Move;

import java.util.UUID;

public record MoveProposedEvent<T extends PieceType>(
        UUID gameId,
        PlayerData player,
        Move<T> move
) implements GameEvent {
}