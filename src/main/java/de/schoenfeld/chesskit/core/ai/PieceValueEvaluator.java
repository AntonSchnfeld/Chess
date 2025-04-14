package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;

public class PieceValueEvaluator<T extends Tile, P extends PieceType> implements GameStateEvaluator<T, P> {
    @Override
    public int evaluate(GameState<T, P> gameState) {
        return gameState.getChessBoard().getTilesWithColour(gameState.getColor())
                .stream()
                .map(square -> gameState.getPieceAt(square).pieceType().value())
                .mapToInt(Integer::intValue)
                .sum();
    }
}
