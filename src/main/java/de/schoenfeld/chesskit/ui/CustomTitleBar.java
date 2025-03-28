package de.schoenfeld.chesskit.ui;

import javax.swing.*;
import java.awt.*;

public class CustomTitleBar extends JPanel {

    public CustomTitleBar(Theme theme) {
        setLayout(new BorderLayout());
        setBackground(theme.titleBarBackground());
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel title = new JLabel("♔ Chess Master 3000 ♚");
        title.setForeground(theme.titleBarText());
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton closeButton = new JButton("✕");
        closeButton.setContentAreaFilled(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        closeButton.setForeground(theme.titleBarButton());
        closeButton.addActionListener(e -> System.exit(0));

        add(title, BorderLayout.WEST);
        add(closeButton, BorderLayout.EAST);
    }
}