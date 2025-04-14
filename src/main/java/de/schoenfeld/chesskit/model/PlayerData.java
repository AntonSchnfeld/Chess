package de.schoenfeld.chesskit.model;

import java.util.UUID;

public record PlayerData(
        UUID playerId,
        String name,
        Color color
) {
}
