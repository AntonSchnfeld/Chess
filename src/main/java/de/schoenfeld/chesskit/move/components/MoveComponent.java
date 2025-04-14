package de.schoenfeld.chesskit.move.components;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;

import java.io.Serializable;

public interface MoveComponent<T extends Tile, P extends PieceType> extends Serializable {
    void makeOn(GameState<T, P> gameState, Move<T, P> move);

    void unmakeOn(GameState<T, P> gameState, Move<T, P> move);
}
