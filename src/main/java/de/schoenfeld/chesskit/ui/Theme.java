package de.schoenfeld.chesskit.ui;

import java.awt.*;

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

    Color squareHighlight();
}