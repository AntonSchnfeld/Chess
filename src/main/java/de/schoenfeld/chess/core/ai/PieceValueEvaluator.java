package de.schoenfeld.chess.core.ai;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;

public class PieceValueEvaluator<T extends PieceType> implements GameStateEvaluator<T> {
    @Override
    public int evaluate(GameState<T> gameState) {
        return gameState.chessBoard().getSquaresWithColour(gameState.isWhiteTurn())
                .stream()
                .map(square -> gameState.getPieceAt(square).pieceType().value())
                .mapToInt(Integer::intValue)
                .sum();
    }
}
