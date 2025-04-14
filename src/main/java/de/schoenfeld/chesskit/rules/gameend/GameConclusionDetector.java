package de.schoenfeld.chesskit.rules.gameend;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.events.GameConclusion;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;

public interface GameConclusionDetector<T extends Tile, P extends PieceType> {
    GameConclusion detectConclusion(GameState<T, P> gameState);
}
