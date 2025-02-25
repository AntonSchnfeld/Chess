package de.schoenfeld.chess;

import de.schoenfeld.chess.pieces.ChessPiece;

public interface ChessBoardView {
    ChessPiece getPieceAt(Position position);

    ChessBoardBounds getChessBoardBounds();

    Position getKingPosition(boolean colour);
}
