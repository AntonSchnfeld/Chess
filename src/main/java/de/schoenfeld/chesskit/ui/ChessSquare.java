package de.schoenfeld.chesskit.ui;

import de.schoenfeld.chesskit.board.tile.Square8x8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChessSquare extends JLabel {
    private final Square8x8 position;
    private final Theme theme;
    private boolean highlight;

    public ChessSquare(Square8x8 position, Theme theme) {
        this.position = position;
        this.theme = theme;
        highlight = false;
        initialize();
    }

    private void initialize() {
        // Ensure the label is transparent
        setOpaque(false);

        // Set up the font with a fallback
        Font chessFont = new Font("Chess Merida Unicode", Font.PLAIN, 48);
        if (!chessFont.getFamily().equalsIgnoreCase("Chess Merida Unicode")) {
            chessFont = new Font(Font.SANS_SERIF, Font.PLAIN, 48); // Fallback font
        }
        setFont(chessFont);

        // Center the text
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

        // Set initial foreground color (for pieces)
        setForeground(theme.whitePiece()); // Overrides default

        // Add padding
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Add hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(theme.highlight(), 2),
                        BorderFactory.createEmptyBorder(3, 3, 3, 3)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            }
        });
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Paint the background
        Graphics2D g2d = (Graphics2D) g.create();

        // Set the base color for the square
        Color baseColor = (position.x() + position.y()) % 2 == 0
                ? theme.lightSquare() : theme.darkSquare();

        // Add a subtle gradient
        GradientPaint gradient = new GradientPaint(
                0, 0, baseColor.brighter(),
                getWidth(), getHeight(), baseColor.darker()
        );
        if (highlight) {
            g2d.setColor(theme.squareHighlight());
        } else g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Add a border
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        // Dispose of the Graphics2D object
        g2d.dispose();

        // Paint the text (piece symbol) on top
        super.paintComponent(g);
    }

    public Square8x8 getPosition() {
        return position;
    }
}