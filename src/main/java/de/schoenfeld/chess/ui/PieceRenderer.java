package de.schoenfeld.chess.ui;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;

import java.awt.*;

import static de.schoenfeld.chess.model.PieceType.*;

public class PieceRenderer {
    private final Theme theme;

    public PieceRenderer(Theme theme) {
        this.theme = theme;
    }

    public String getSymbol(ChessPiece piece) {
        PieceType type = piece.pieceType();
        if (type.equals(KING)) return "♚";
        if (type.equals(QUEEN)) return "♛";
        if (type.equals(ROOK)) return "♜";
        if (type.equals(BISHOP)) return "♝";
        if (type.equals(KNIGHT)) return "♞";
        if (type.equals(PAWN)) return "♟";
        return piece.pieceType().symbol();
    }

    public Color getPieceColor(ChessPiece piece) {
        return piece.isWhite() ? theme.whitePiece() : theme.blackPiece();
    }
}