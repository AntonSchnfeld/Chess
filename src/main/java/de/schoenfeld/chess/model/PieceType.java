package de.schoenfeld.chess.model;

import java.io.Serializable;

public interface PieceType extends Serializable {
    String symbol();

    int value();

    boolean isKing();
}