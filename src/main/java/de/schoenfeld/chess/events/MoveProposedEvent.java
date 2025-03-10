package de.schoenfeld.chess.events;

import de.schoenfeld.chess.model.PlayerData;
import de.schoenfeld.chess.move.Move;

import java.util.UUID;

public record MoveProposedEvent(
        UUID gameId,
        PlayerData player,
        Move move
) implements GameEvent {
}