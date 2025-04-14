package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveLookup;

@FunctionalInterface
public interface GenerativeMoveRule<T extends Tile, P extends PieceType> {
    void generateMoves(GameState<T, P> gameState, MoveLookup<T, P> moves);
}
