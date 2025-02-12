package org.example;

import org.example.pieces.ChessPiece;

import java.util.Map;

public interface ChessBoardView {
    Map<Position, ChessPiece> getPiecePositions();
    ChessBoardDimensions getChessBoardDimensions();
}
