package org.example;

import org.example.pieces.ChessPiece;

public interface ChessBoardView {
    ChessPiece getPieceAt(Position position);

    ChessBoardBounds getChessBoardBounds();
}
