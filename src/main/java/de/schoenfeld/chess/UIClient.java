package de.schoenfeld.chess;

import de.schoenfeld.chess.events.EventBus;
import de.schoenfeld.chess.events.GameStateChangedEvent;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class UIClient {
    private final EventBus eventBus;
    private final Map<Position, JLabel> boardSquares = new HashMap<>();
    private final JFrame frame = new JFrame("Chess Viewer");
    private final Color lightColor = new Color(0, 0, 0);
    private final Color darkColor = new Color(0, 5, 61);
    private final Color WHITE = new Color(0, 255, 225);
    private final Color BLACK = new Color(255, 255, 255);

    public UIClient(EventBus eventBus) {
        this.eventBus = eventBus;
        initializeUI();
        registerEventHandlers();
    }

    private void initializeUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        JPanel chessBoard = new JPanel(new GridLayout(8, 8));
        chessBoard.setPreferredSize(new Dimension(560, 560));

        // Create chess board squares
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                Position pos = new Position(x, y);
                JLabel square = createBoardSquare(pos);
                boardSquares.put(pos, square);
                chessBoard.add(square);
            }
        }

        frame.add(chessBoard, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JLabel createBoardSquare(Position pos) {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setBackground((pos.x() + pos.y()) % 2 == 0 ? lightColor : darkColor);
        label.setFont(new Font("Arial Unicode MS", Font.PLAIN, 40));
        label.setPreferredSize(new Dimension(70, 70));
        return label;
    }

    private void registerEventHandlers() {
        eventBus.subscribe(GameStateChangedEvent.class, this::handleGameStateChanged);
    }

    private void handleGameStateChanged(GameStateChangedEvent event) {
        Runnable updateTask = () -> {
            // Clear all squares first
            boardSquares.values().forEach(label -> {
                label.setText("");
                label.setForeground(BLACK);
            });

            // Update with new state
            event.newState().chessBoard().getPieces().forEach((piece) -> {
                Position pos = event.newState().chessBoard().getPiecePosition(piece);
                JLabel square = boardSquares.get(pos);
                square.setText(getPieceSymbol(piece));
                square.setForeground(piece.isWhite() ? WHITE : BLACK);
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
        PieceType pt = piece.getPieceType();

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