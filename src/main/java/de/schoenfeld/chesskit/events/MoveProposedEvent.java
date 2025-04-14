package de.schoenfeld.chesskit.events;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.PlayerData;
import de.schoenfeld.chesskit.move.Move;

import java.util.UUID;

public record MoveProposedEvent<T extends Tile, P extends PieceType>(
        UUID gameId,
        PlayerData player,
        Move<T, P> move
) implements GameEvent {
}