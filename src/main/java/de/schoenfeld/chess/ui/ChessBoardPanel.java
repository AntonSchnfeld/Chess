package de.schoenfeld.chess.ui;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ChessBoardPanel extends JPanel {
    private final Map<Square, ChessSquare> squares = new HashMap<>();
    private final PieceRenderer pieceRenderer;
    private final Theme theme;

    public ChessBoardPanel(PieceRenderer pieceRenderer, Theme theme) {
        this.pieceRenderer = pieceRenderer;
        this.theme = theme;
        setLayout(new GridLayout(8, 8, 2, 2));
        initializeSquares();
    }

    private void initializeSquares() {
        // Ensure coordinates match the game engine's system
        for (int rank = 0; rank < 8; rank++) {  // Ranks: 0 (1st rank) to 7 (8th rank)
            for (int file = 0; file < 8; file++) {  // Files: 0 (a-file) to 7 (h-file)
                Square pos = new Square(file, rank);
                ChessSquare square = new ChessSquare(pos, theme);
                squares.put(pos, square);
                add(square);
            }
        }
    }

    public <T extends PieceType> void updateBoard(Map<Square, ChessPiece<T>> positions) {
        // Debugging: Print positions being updated

        // Update only the squares that have pieces
        squares.forEach((pos, square) -> {
            ChessPiece<T> piece = positions.get(pos);
            if (piece != null) {
                // Debugging: Print piece details
                square.setForeground(pieceRenderer.getPieceColor(piece));
                square.setText(pieceRenderer.getSymbol(piece));
            } else {
                square.setText("");
            }
        });

        // Force immediate repaint
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640, 640);
    }
}