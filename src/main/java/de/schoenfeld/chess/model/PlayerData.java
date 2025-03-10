package de.schoenfeld.chess.model;

import java.util.UUID;

public record PlayerData(
        UUID playerId,
        String name,
        boolean isWhite
) {
}
