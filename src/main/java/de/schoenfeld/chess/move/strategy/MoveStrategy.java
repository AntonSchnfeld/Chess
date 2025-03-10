package de.schoenfeld.chess.move.strategy;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.MoveCollection;

import java.io.Serializable;

public interface MoveStrategy extends Serializable {
    MoveCollection getPseudoLegalMoves(ImmutableChessBoard chessBoard, Position pos);
}
