package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ChessBoard extends Serializable {
    ChessPiece getPieceAt(Square square);

    Square getPiecePosition(ChessPiece chessPiece);

    ChessBoardBounds getBounds();

    List<ChessPiece> getPiecesOfColour(boolean isWhite);

    List<ChessPiece> getPieces();

    List<ChessPiece> getPiecesOfTypeAndColour(PieceType pieceType, boolean isWhite);

    List<ChessPiece> getPiecesOfType(PieceType pieceType);

    String toFen();

    ChessBoard withPieceAt(ChessPiece piece, Square square);

    ChessBoard withoutPieceAt(Square square);

    ChessBoard withPieceMoved(Square from, Square to);

    ChessBoard withAllPieces(Map<Square, ChessPiece> pieces);

    ChessBoard withoutPieces();

    ChessBoard withBounds(ChessBoardBounds bounds);
}