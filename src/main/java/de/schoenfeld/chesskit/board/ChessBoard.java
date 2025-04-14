package de.schoenfeld.chesskit.board;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.PieceType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ChessBoard<T extends Tile, P extends PieceType> extends Serializable {
    ChessPiece<P> getPieceAt(T square);

    ChessBoardBounds<T> getBounds();

    void setBounds(ChessBoardBounds<T> bounds);

    List<T> getTilesWithColour(Color color);

    List<T> getOccupiedTiles();

    List<T> getTilesWithTypeAndColour(P pieceType, Color color);

    List<T> getTilesWithType(P pieceType);

    boolean isOccupied(T tile);

    void setPieceAt(T tile, ChessPiece<P> piece);

    void removePieceAt(T tile);

    void movePiece(T from, T to);

    void setAllPieces(Map<T, ChessPiece<P>> pieces);

    void removePieces();
}