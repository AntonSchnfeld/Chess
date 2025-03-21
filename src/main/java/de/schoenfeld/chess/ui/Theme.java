package de.schoenfeld.chess.ui;

import java.awt.Color;

public interface Theme {
    Color lightSquare();
    Color darkSquare();
    Color highlight();
    Color whitePiece();
    Color blackPiece();
    Color[] backgroundGradient();
    Color coordinateText();
    Color titleBarBackground();
    Color titleBarText();
    Color titleBarButton();
}