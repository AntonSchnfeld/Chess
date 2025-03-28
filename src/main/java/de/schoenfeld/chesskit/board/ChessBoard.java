package de.schoenfeld.chesskit.board;

import de.schoenfeld.chesskit.model.ChessBoardBounds;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.Square;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ChessBoard<T extends PieceType> extends Serializable {
    ChessPiece<T> getPieceAt(Square square);

    ChessBoardBounds getBounds();

    void setBounds(ChessBoardBounds bounds);

    List<Square> getSquaresWithColour(boolean isWhite);

    List<Square> getOccupiedSquares();

    List<Square> getSquaresWithTypeAndColour(T pieceType, boolean isWhite);

    List<Square> getSquaresWithType(T pieceType);

    boolean isOccupied(Square square);

    String toFen();

    void setPieceAt(Square square, ChessPiece<T> piece);

    void removePieceAt(Square square);

    void movePiece(Square from, Square to);

    void setAllPieces(Map<Square, ChessPiece<T>> pieces);

    void removePieces();
}