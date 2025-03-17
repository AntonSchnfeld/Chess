package de.schoenfeld.chess.board;

import de.schoenfeld.chess.model.ChessBoardBounds;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ImmutableChessBoard extends Serializable {
    ChessPiece getPieceAt(Position position);

    Position getPiecePosition(ChessPiece chessPiece);

    ChessBoardBounds getBounds();

    List<ChessPiece> getPiecesOfColour(boolean isWhite);

    List<ChessPiece> getPieces();

    List<ChessPiece> getPiecesOfType(PieceType pieceType, boolean isWhite);

    String toFen();

    ImmutableChessBoard withPieceAt(ChessPiece piece, Position position);

    ImmutableChessBoard withoutPieceAt(Position position);

    ImmutableChessBoard withPieceMoved(Position from, Position to);

    ImmutableChessBoard withAllPieces(Map<Position, ChessPiece> pieces);

    ImmutableChessBoard withoutPieces();

    ImmutableChessBoard withBounds(ChessBoardBounds bounds);
}