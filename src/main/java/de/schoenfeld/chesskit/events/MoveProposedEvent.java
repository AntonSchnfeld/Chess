package de.schoenfeld.chesskit.events;

import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.PlayerData;
import de.schoenfeld.chesskit.move.Move;

import java.util.UUID;

public record MoveProposedEvent<T extends PieceType>(
        UUID gameId,
        PlayerData player,
        Move<T> move
) implements GameEvent {
}