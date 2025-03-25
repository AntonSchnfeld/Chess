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

    void setPieceAt(ChessPiece<T> piece, Square square);

    void removePieceAt(Square square);

    void movePiece(Square from, Square to);

    void setAllPieces(Map<Square, ChessPiece<T>> pieces);

    void removePieces();

    void setBounds(ChessBoardBounds bounds);
}