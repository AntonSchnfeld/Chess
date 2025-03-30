package de.schoenfeld.chesskit.ui;

import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.StandardPieceType;

import java.awt.*;

public class PieceRenderer {
    private final Theme theme;

    public PieceRenderer(Theme theme) {
        this.theme = theme;
    }

    public String getSymbol(ChessPiece<?> piece) {
        PieceType type = piece.pieceType();
        return switch (type) {
            case StandardPieceType.KING -> "♚";
            case StandardPieceType.QUEEN -> "♛";
            case StandardPieceType.ROOK -> "♜";
            case StandardPieceType.BISHOP -> "♝";
            case StandardPieceType.KNIGHT -> "♞";
            case StandardPieceType.PAWN -> "♟";
            default -> piece.pieceType().symbol();
        };
    }

    public Color getPieceColor(ChessPiece<?> piece) {
        return piece.isWhite() ? theme.whitePiece() : theme.blackPiece();
    }
}