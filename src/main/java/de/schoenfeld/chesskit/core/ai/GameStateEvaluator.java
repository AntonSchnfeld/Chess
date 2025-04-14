package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;

@FunctionalInterface
public interface GameStateEvaluator<T extends Tile, P extends PieceType> {
    int evaluate(GameState<T, P> gameState);
}
