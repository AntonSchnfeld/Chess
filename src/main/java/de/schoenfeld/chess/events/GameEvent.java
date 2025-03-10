package de.schoenfeld.chess.events;

import java.io.Serializable;
import java.util.UUID;

public interface GameEvent extends Serializable {
    UUID gameId();
}
