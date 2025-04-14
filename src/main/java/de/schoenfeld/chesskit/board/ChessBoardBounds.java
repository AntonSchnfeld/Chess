package de.schoenfeld.chesskit.board;

import de.schoenfeld.chesskit.board.tile.Tile;

import java.util.Collection;

public interface ChessBoardBounds<T extends Tile> {
    boolean contains(T tile);
    Collection<T> getTiles();
    int getTileCount();
}
