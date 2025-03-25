package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.move.Move;

public record CastlingComponent<T extends PieceType>(Square from, Square to)
        implements MoveComponent<T> {

    @Override
    public void executeOn(GameState<T> gameState, Move<T> move) {
        gameState.movePiece(from, to);
    }

    @Override
    public void undoOn(GameState<T> gameState, Move<T> move) {
        gameState.movePiece(to, from);
    }
}
