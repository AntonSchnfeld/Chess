package de.schoenfeld.chesskit.move.components;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;

public record CastlingComponent<T extends Tile, P extends PieceType>(T from, T to)
        implements MoveComponent<T, P> {

    @Override
    public void makeOn(GameState<T, P> gameState, Move<T, P> move) {
        gameState.movePiece(from, to);
    }

    @Override
    public void unmakeOn(GameState<T, P> gameState, Move<T, P> move) {
        gameState.movePiece(to, from);
    }
}
