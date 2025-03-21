package de.schoenfeld.chess.ui;

import de.schoenfeld.chess.events.EventBus;
import de.schoenfeld.chess.events.GameStateChangedEvent;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.Square;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
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
        initializeUI();
        registerEventHandlers();
    }

    private void initializeUI() {
        frame.setUndecorated(true);
        frame.setShape(new RoundRectangle2D.Double(0, 0, 800, 800, 30, 30));
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel contentPane = getJPanel();
        contentPane.setFont(Font.getFont(Font.SANS_SERIF));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(new CoordinatesPanel(true, theme), BorderLayout.EAST);
        wrapper.add(new CoordinatesPanel(false, theme), BorderLayout.SOUTH);
        wrapper.add(boardPanel, BorderLayout.CENTER);

        contentPane.add(wrapper, BorderLayout.CENTER);
        frame.add(contentPane);
        frame.add(new CustomTitleBar(theme), BorderLayout.NORTH);
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

    private void handleGameStateChanged(GameStateChangedEvent<?> event) {
        SwingUtilities.invokeLater(() -> {
            try {
                Map<Square, ChessPiece> positions = new HashMap<>();
                event.newState().chessBoard().getPieces().forEach(piece -> {
                    Square pos = event.newState().chessBoard().getPiecePosition(piece);
                    positions.put(pos, piece);
                });
                boardPanel.updateBoard(positions);
                frame.revalidate();
                frame.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }
}