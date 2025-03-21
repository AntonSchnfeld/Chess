package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ChessBoard<T extends PieceType> extends Serializable {
    ChessPiece<T> getPieceAt(Square square);

    Square getPiecePosition(ChessPiece<T> chessPiece);

    ChessBoardBounds getBounds();

    List<ChessPiece<T>> getPiecesOfColour(boolean isWhite);

    List<ChessPiece<T>> getPieces();

    List<ChessPiece<T>> getPiecesOfTypeAndColour(T pieceType, boolean isWhite);

    List<ChessPiece<T>> getPiecesOfType(T pieceType);

    String toFen();

    ChessBoard<T> withPieceAt(ChessPiece<T> piece, Square square);

    ChessBoard<T> withoutPieceAt(Square square);

    ChessBoard<T> withPieceMoved(Square from, Square to);

    ChessBoard<T> withAllPieces(Map<Square, ChessPiece<T>> pieces);

    ChessBoard<T> withoutPieces();

    ChessBoard<T> withBounds(ChessBoardBounds bounds);
}