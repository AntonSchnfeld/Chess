package de.schoenfeld.chess;

import de.schoenfeld.chess.events.EventBus;
import de.schoenfeld.chess.events.GameStateChangedEvent;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


public class UIClient {
    // Updated color scheme
    private final Color lightColor = new Color(232, 235, 239);  // Light marble
    private final Color darkColor = new Color(94, 129, 172);    // Dark slate
    private final Color highlightColor = new Color(255, 215, 0, 100); // Gold highlight
    private final Color pieceWhite = new Color(255, 255, 255);  // Pure white
    private final Color pieceBlack = new Color(40, 40, 40);      // Soft black

    private final Map<Position, JLabel> boardSquares = new HashMap<>();
    private final JFrame frame = new JFrame("Chess Viewer");

    // Gradient colors for background
    private final Color[] backgroundGradient = {
            new Color(23, 32, 42),
            new Color(44, 62, 80)
    };

    private EventBus eventBus;

    public UIClient(EventBus eventBus) {
        this.eventBus = eventBus;
        initializeUI();
        registerEventHandlers();
    }

    private void registerEventHandlers() {
        eventBus.subscribe(GameStateChangedEvent.class, this::handleGameStateChanged);
    }

    private void initializeUI() {
        frame.setUndecorated(true);
        frame.setShape(new RoundRectangle2D.Double(0, 0, 800, 800, 30, 30));
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Add shadow effect
        ((JComponent) frame.getContentPane()).setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 4, 4, 4, new Color(0, 0, 0, 50)),
                        BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(255, 255, 255, 50))
                )
        );

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();

                // Paint gradient background
                GradientPaint gp = new GradientPaint(0, 0, backgroundGradient[0], w, h, backgroundGradient[1]);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel chessBoard = new JPanel(new GridLayout(8, 8, 2, 2)) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(640, 640);
            }
        };

        // Create chess board squares with 3D effect
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                Position pos = new Position(x, y);
                JLabel square = createBoardSquare(pos);
                boardSquares.put(pos, square);
                chessBoard.add(square);
            }
        }

        // Add coordinates
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(createCoordinatePanel(true), BorderLayout.EAST);
        wrapper.add(createCoordinatePanel(false), BorderLayout.SOUTH);
        wrapper.add(chessBoard, BorderLayout.CENTER);

        mainPanel.add(wrapper, BorderLayout.CENTER);
        frame.add(mainPanel);

        // Add custom title bar
        JPanel titleBar = createTitleBar();
        frame.add(titleBar, BorderLayout.NORTH);
    }

    private JLabel createBoardSquare(Position pos) {
        JLabel label = new JLabel("", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();

                // Paint background with subtle gradient
                Color baseColor = (pos.x() + pos.y()) % 2 == 0 ? lightColor : darkColor;
                GradientPaint gp = new GradientPaint(
                        0, 0, baseColor.brighter(),
                        getWidth(), getHeight(), baseColor.darker()
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Add inner shadow
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        label.setOpaque(false);
        label.setFont(new Font("Chess Merida Unicode", Font.PLAIN, 48));
        label.setForeground(new Color(0, 0, 0, 0)); // Transparent text (shadow)
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Add hover effect
        label.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(highlightColor, 2),
                        BorderFactory.createEmptyBorder(3, 3, 3, 3)
                ));
            }

            public void mouseExited(MouseEvent e) {
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            }
        });

        return label;
    }

    private JPanel createCoordinatePanel(boolean vertical) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        if (vertical) {
            panel.setLayout(new GridLayout(8, 1));
            for (int i = 8; i >= 1; i--) {
                JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
                styleCoordinateLabel(label);
                panel.add(label);
            }
        } else {
            panel.setLayout(new GridLayout(1, 8));
            for (char c = 'A'; c <= 'H'; c++) {
                JLabel label = new JLabel(String.valueOf(c), SwingConstants.CENTER);
                styleCoordinateLabel(label);
                panel.add(label);
            }
        }
        return panel;
    }

    private void styleCoordinateLabel(JLabel label) {
        label.setForeground(new Color(255, 255, 255, 150));
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    private JPanel createTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(0, 0, 0, 100));
        titleBar.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel title = new JLabel("♔ Chess Master 3000 ♚");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton closeButton = new JButton("✕");
        closeButton.setContentAreaFilled(false);
        closeButton.setBorder(new EmptyBorder(5, 10, 5, 10));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        closeButton.addActionListener(e -> System.exit(0));

        titleBar.add(title, BorderLayout.WEST);
        titleBar.add(closeButton, BorderLayout.EAST);

        return titleBar;
    }


    private void handleGameStateChanged(GameStateChangedEvent event) {
        Runnable updateTask = () -> {
            // Clear all squares first
            boardSquares.values().forEach(label -> {
                label.setText("");
                label.setForeground(darkColor);
            });

            // Update with new state
            event.newState().chessBoard().getPieces().forEach((piece) -> {
                Position pos = event.newState().chessBoard().getPiecePosition(piece);
                JLabel square = boardSquares.get(pos);
                square.setText(getPieceSymbol(piece));
                square.setForeground(piece.isWhite() ? pieceWhite : pieceBlack);
            });

            // Force immediate repaint
            frame.revalidate();
            frame.repaint();
        };

        if (SwingUtilities.isEventDispatchThread()) {
            updateTask.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(updateTask);
            } catch (InterruptedException | InvocationTargetException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("UI update failed", e);
            }
        }
    }


    private String getPieceSymbol(ChessPiece piece) {
        PieceType pt = piece.pieceType();

        if (pt.equals(PieceType.KING)) return "♔";
        else if (pt.equals(PieceType.QUEEN)) return "♕";
        else if (pt.equals(PieceType.ROOK)) return "♖";
        else if (pt.equals(PieceType.BISHOP)) return "♗";
        else if (pt.equals(PieceType.KNIGHT)) return "♘";
        else if (pt.equals(PieceType.PAWN)) return "♙";
        else return "";
    }

    public void show() {
        frame.setVisible(true);
    }
}