package de.schoenfeld.chess.logic.piece.strategy;

import de.schoenfeld.chess.Position;
import de.schoenfeld.chess.data.ReadOnlyChessBoard;
import de.schoenfeld.chess.data.move.MoveCollection;

public interface MoveStrategy {
    MoveCollection getPseudoLegalMoves(ReadOnlyChessBoard chessBoard, Position pos);
}
