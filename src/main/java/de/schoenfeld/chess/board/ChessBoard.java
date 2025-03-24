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

    ChessBoardBounds getBounds();

    List<Square> getSquaresWithColour(boolean isWhite);

    List<Square> getOccupiedSquares();

    List<Square> getSquaresWithTypeAndColour(T pieceType, boolean isWhite);

    List<Square> getSquaresWithType(T pieceType);

    boolean isOccupied(Square square);

    String toFen();

    ChessBoard<T> withPieceAt(ChessPiece<T> piece, Square square);

    ChessBoard<T> withoutPieceAt(Square square);

    ChessBoard<T> withPieceMoved(Square from, Square to);

    ChessBoard<T> withAllPieces(Map<Square, ChessPiece<T>> pieces);

    ChessBoard<T> withoutPieces();

    ChessBoard<T> withBounds(ChessBoardBounds bounds);
}