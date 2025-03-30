package de.schoenfeld.chesskit.ui;

import java.awt.*;

public class DefaultTheme implements Theme {
    @Override
    public Color lightSquare() {
        return new Color(232, 235, 239);
    }

    @Override
    public Color darkSquare() {
        return new Color(94, 129, 172);
    }

    @Override
    public Color highlight() {
        return new Color(255, 215, 0, 100);
    }

    @Override
    public Color whitePiece() {
        return Color.WHITE;
    }

    @Override
    public Color blackPiece() {
        return new Color(40, 40, 40);
    }

    @Override
    public Color[] backgroundGradient() {
        return new Color[]{
                new Color(23, 32, 42),
                new Color(44, 62, 80)
        };
    }

    @Override
    public Color coordinateText() {
        return new Color(255, 255, 255, 150);
    }

    @Override
    public Color titleBarBackground() {
        return new Color(0, 0, 0, 100);
    }

    @Override
    public Color titleBarText() {
        return Color.WHITE;
    }

    @Override
    public Color titleBarButton() {
        return Color.WHITE;
    }

    @Override
    public Color squareHighlight() {
        return new Color(0, 255, 103);
    }
}