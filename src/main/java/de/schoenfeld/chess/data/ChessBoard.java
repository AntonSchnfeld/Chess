package de.schoenfeld.chess.data;

import de.schoenfeld.chess.Position;
import de.schoenfeld.chess.logic.piece.ChessPiece;

public interface ChessBoard extends ReadOnlyChessBoard {
    void setPiece(ChessPiece piece, Position position);

    void removePieceAt(Position position);
}
