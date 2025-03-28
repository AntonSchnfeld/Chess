package de.schoenfeld.chesskit.ui;

import javax.swing.*;
import java.awt.*;

public class CoordinatesPanel extends JPanel {
    private final Theme theme;

    public CoordinatesPanel(boolean vertical, Theme theme) {
        this.theme = theme;
        setOpaque(false);
        if (vertical) {
            setLayout(new GridLayout(8, 1));
            for (int i = 8; i >= 1; i--) add(createLabel(String.valueOf(i)));
        } else {
            setLayout(new GridLayout(1, 8));
            for (char c = 'A'; c <= 'H'; c++) add(createLabel(String.valueOf(c)));
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(theme.coordinateText());
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }
}