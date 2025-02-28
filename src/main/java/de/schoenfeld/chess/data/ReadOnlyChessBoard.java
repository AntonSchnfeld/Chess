package de.schoenfeld.chess.data;

import de.schoenfeld.chess.logic.piece.ChessPiece;
import de.schoenfeld.chess.logic.piece.PieceType;

import java.util.List;

public interface ReadOnlyChessBoard {
    ChessPiece getPieceAt(Position position);

    Position getPiecePosition(ChessPiece chessPiece);

    ChessBoardBounds getBounds();

    List<ChessPiece> getPieces();

    List<ChessPiece> getPiecesOfType(PieceType pieceType, boolean colour);

    String toFen();
}
