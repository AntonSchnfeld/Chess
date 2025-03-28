package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;

public class PieceValueEvaluator<T extends PieceType> implements GameStateEvaluator<T> {
    @Override
    public int evaluate(GameState<T> gameState) {
        return gameState.getChessBoard().getSquaresWithColour(gameState.isWhiteTurn())
                .stream()
                .map(square -> gameState.getPieceAt(square).pieceType().value())
                .mapToInt(Integer::intValue)
                .sum();
    }
}
