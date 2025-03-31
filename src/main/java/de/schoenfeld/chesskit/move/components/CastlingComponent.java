package de.schoenfeld.chesskit.move.components;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.Square;
import de.schoenfeld.chesskit.move.Move;

public record CastlingComponent<T extends PieceType>(Square from, Square to)
        implements MoveComponent<T> {

    @Override
    public void makeOn(GameState<T> gameState, Move<T> move) {
        gameState.movePiece(from, to);
    }

    @Override
    public void unmakeOn(GameState<T> gameState, Move<T> move) {
        gameState.movePiece(to, from);
    }
}
