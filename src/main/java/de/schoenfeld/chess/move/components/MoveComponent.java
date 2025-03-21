package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.Move;

import java.io.Serializable;

public interface MoveComponent extends Serializable {
    ChessBoard executeOn(GameState gameState, Move move);
}
