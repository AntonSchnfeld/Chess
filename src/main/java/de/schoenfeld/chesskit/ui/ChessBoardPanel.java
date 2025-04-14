package de.schoenfeld.chesskit.ui;

import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.board.tile.Square8x8;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ChessBoardPanel extends JPanel {
    private final Map<Square8x8, ChessSquare> squares = new HashMap<>();
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
        for (int rank = 0; rank < 8; rank++) {  // Ranks: 0 (1st rank) to 7 (8th ranks)
            for (int file = 0; file < 8; file++) {  // Files: 0 (a-file) to 7 (h-file)
                Square8x8 pos = Square8x8.of(file, rank);
                ChessSquare square = new ChessSquare(pos, theme);
                squares.put(pos, square);
                add(square);
            }
        }
    }

    public Map<Square8x8, ChessSquare> getSquares() {
        return squares;
    }

    public void setSquareHighlight(Square8x8 square8x8, boolean enabled) {
        squares.get(square8x8).setHighlight(enabled);
        repaint();
        updateUI();
    }

    public void clearHighlights() {
        for (Map.Entry<Square8x8, ChessSquare> entry : squares.entrySet())
            entry.getValue().setHighlight(false);
        repaint();
        updateUI();
    }

    public <T extends PieceType> void updateBoard(Map<Square8x8, ChessPiece<T>> positions) {
        // Debugging: print positions being updated

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