package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.Move;

import java.io.Serializable;

public interface MoveComponent extends Serializable {
    ImmutableChessBoard executeOn(GameState gameState, Move move);
}
