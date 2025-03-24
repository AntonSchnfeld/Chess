package de.schoenfeld.chess.model;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.board.MapChessBoard;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record GameState<T extends PieceType>(
        ChessBoard<T> chessBoard,
        MoveHistory<T> moveHistory,
        boolean isWhiteTurn
) implements ChessBoard<T>, Serializable {

    public GameState {
        Objects.requireNonNull(chessBoard, "chessBoard cannot be null");
        Objects.requireNonNull(moveHistory, "moveHistory cannot be null");
    }

    public GameState(ChessBoard<T> chessBoard) {
        this(chessBoard, new MoveHistory<>(), true);
    }

    public GameState() {
        this(new MapChessBoard<>(new ChessBoardBounds(8, 8)), new MoveHistory<>());
    }

    public GameState(ChessBoard<T> chessBoard, MoveHistory<T> moveHistory) {
        this(chessBoard, moveHistory, true);
    }

    @Override
    public boolean isOccupied(Square square) {
        return chessBoard.isOccupied(square);
    }

    // State transition methods optimized for performance
    public GameState<T> withIsWhiteTurn(boolean newTurn) {
        return (this.isWhiteTurn == newTurn) ?
                this : new GameState<>(chessBoard, moveHistory, newTurn);
    }

    public GameState<T> withTurnSwitched() {
        return new GameState<>(chessBoard, moveHistory, !isWhiteTurn);
    }

    public GameState<T> withChessBoard(ChessBoard<T> newBoard) {
        return (this.chessBoard == newBoard) ? this : new GameState<>(newBoard, moveHistory, isWhiteTurn);
    }

    public GameState<T> withMoveHistory(MoveHistory<T> newHistory) {
        return (this.moveHistory == newHistory) ? this : new GameState<>(chessBoard, newHistory, isWhiteTurn);
    }

    public GameState<T> previousState() {
        return moveHistory.getMoveCount() == 0 ? this : new GameState<>(chessBoard, moveHistory.withoutLastMove(), isWhiteTurn);
    }

    @Override
    public ChessPiece<T> getPieceAt(Square square) {
        return chessBoard.getPieceAt(square);
    }

    @Override
    public ChessBoardBounds getBounds() {
        return chessBoard.getBounds();
    }

    @Override
    public List<Square> getSquaresWithColour(boolean isWhite) {
        return chessBoard.getSquaresWithColour(isWhite);
    }

    @Override
    public List<Square> getOccupiedSquares() {
        return chessBoard.getOccupiedSquares();
    }

    @Override
    public List<Square> getSquaresWithTypeAndColour(T pieceType, boolean isWhite) {
        return chessBoard.getSquaresWithTypeAndColour(pieceType, isWhite);
    }

    @Override
    public List<Square> getSquaresWithType(T pieceType) {
        return chessBoard.getSquaresWithType(pieceType);
    }

    @Override
    public String toFen() {
        return chessBoard.toFen();
    }

    @Override
    public GameState<T> withPieceAt(ChessPiece<T> piece, Square square) {
        return withChessBoard(chessBoard.withPieceAt(piece, square));
    }

    @Override
    public ChessBoard<T> withoutPieceAt(Square square) {
        return withChessBoard(chessBoard.withoutPieceAt(square));
    }

    @Override
    public ChessBoard<T> withPieceMoved(Square from, Square to) {
        return withChessBoard(chessBoard.withPieceMoved(from, to));
    }

    @Override
    public ChessBoard<T> withAllPieces(Map<Square, ChessPiece<T>> pieces) {
        return withChessBoard(chessBoard.withAllPieces(pieces));
    }

    @Override
    public ChessBoard<T> withoutPieces() {
        return withChessBoard(chessBoard.withoutPieces());
    }

    @Override
    public ChessBoard<T> withBounds(ChessBoardBounds bounds) {
        return withChessBoard(chessBoard.withBounds(bounds));
    }
}