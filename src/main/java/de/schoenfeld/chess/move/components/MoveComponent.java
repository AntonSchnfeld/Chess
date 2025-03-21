package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.Move;

import java.io.Serializable;

public interface MoveComponent extends Serializable {
    <T extends PieceType> ChessBoard<T> executeOn(GameState<T> gameState, Move move);
}
