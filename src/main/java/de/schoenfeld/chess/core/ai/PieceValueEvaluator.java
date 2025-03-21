package de.schoenfeld.chess.core.ai;

import de.schoenfeld.chess.model.GameState;

public class PieceValueEvaluator implements GameStateEvaluator {
    @Override
    public int evaluate(GameState gameState) {
        return gameState.chessBoard().getPiecesOfColour(gameState.isWhiteTurn())
                .stream()
                .map(p -> p.pieceType().value())
                .mapToInt(Integer::intValue)
                .sum();
    }
}
