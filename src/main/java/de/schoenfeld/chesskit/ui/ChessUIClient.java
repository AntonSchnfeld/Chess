package de.schoenfeld.chesskit.ui;

import de.schoenfeld.chesskit.events.EventBus;
import de.schoenfeld.chesskit.events.GameStateChangedEvent;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.Square;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ChessUIClient {
    private final JFrame frame = new JFrame("Chess Viewer");
    private final ChessBoardPanel boardPanel;
    private final EventBus eventBus;
    private final Theme theme;

    public ChessUIClient(EventBus eventBus, PieceRenderer renderer, Theme theme) {
        this.theme = theme;
        this.eventBus = eventBus;
        this.boardPanel = new ChessBoardPanel(renderer, theme);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initializeUI();
        registerEventHandlers();
    }

    public ChessBoardPanel getBoardPanel() {
        return boardPanel;
    }

    private void initializeUI() {
        //frame.setUndecorated(true);
        //frame.setShape(new RoundRectangle2D.Double(0, 0, 800, 800, 30, 30));
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        boardPanel.setFocusable(true);
        boardPanel.setOpaque(true);

        JPanel contentPane = getJPanel();
        contentPane.setFont(Font.getFont(Font.SANS_SERIF));

        contentPane.add(boardPanel, BorderLayout.CENTER);
        frame.setContentPane(contentPane);
        boardPanel.requestFocusInWindow();
    }

    private JPanel getJPanel() {
        JPanel contentPane = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                GradientPaint gradient = new GradientPaint(
                        0, 0, theme.backgroundGradient()[0],
                        getWidth(), getHeight(), theme.backgroundGradient()[1]
                );

                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return contentPane;
    }

    private void registerEventHandlers() {
        eventBus.subscribe(GameStateChangedEvent.class, this::handleGameStateChanged);
    }

    private <T extends PieceType> void handleGameStateChanged(GameStateChangedEvent<T> event) {
        SwingUtilities.invokeLater(() -> {
            try {
                Map<Square, ChessPiece<T>> squares = new HashMap<>();
                event.newState().getOccupiedSquares().forEach(square -> {
                    squares.put(square, event.newState().getPieceAt(square));
                });
                boardPanel.updateBoard(squares);
                frame.revalidate();
                frame.repaint();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    public Square getSquareAt(int x, int y) {
        int boardSize = boardPanel.getWidth(); // Assuming square board
        int squareSize = boardSize / 8; // Each square's size

        int col = (x - boardPanel.getX()) / squareSize;
        int row = (y - boardPanel.getY()) / squareSize;

        if (col < 0 || col >= 8 || row < 0 || row >= 8) {
            return null; // Click is outside the board
        }

        return Square.of(col, 7 - row); // Convert to chess coordinates
    }
}