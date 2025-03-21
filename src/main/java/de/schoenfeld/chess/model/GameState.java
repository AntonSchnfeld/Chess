package de.schoenfeld.chess.model;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.board.MapChessBoard;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record GameState(
        ChessBoard chessBoard,
        MoveHistory moveHistory,
        boolean isWhiteTurn
) implements ChessBoard, Serializable {
    private static final GameState INITIAL_STATE = new GameState(
            new MapChessBoard(new ChessBoardBounds(8, 8)),
            new MoveHistory(),
            true
    );

    public GameState {
        Objects.requireNonNull(chessBoard, "chessBoard cannot be null");
        Objects.requireNonNull(moveHistory, "moveHistory cannot be null");
    }

    public GameState(ChessBoard chessBoard) {
        this(chessBoard, new MoveHistory(), true);
    }

    public GameState(ChessBoard chessBoard, MoveHistory moveHistory) {
        this(chessBoard, moveHistory, true);
    }

    public GameState() {
        this(INITIAL_STATE.chessBoard, INITIAL_STATE.moveHistory, true);
    }

    public static GameState createInitial() {
        return INITIAL_STATE;
    }

    // State transition methods optimized for performance
    public GameState withIsWhiteTurn(boolean newTurn) {
        return (this.isWhiteTurn == newTurn) ? this : new GameState(chessBoard, moveHistory, newTurn);
    }

    public GameState withTurnSwitched() {
        return new GameState(chessBoard, moveHistory, !isWhiteTurn);
    }

    public GameState withChessBoard(ChessBoard newBoard) {
        return (this.chessBoard == newBoard) ? this : new GameState(newBoard, moveHistory, isWhiteTurn);
    }

    public GameState withMoveHistory(MoveHistory newHistory) {
        return (this.moveHistory == newHistory) ? this : new GameState(chessBoard, newHistory, isWhiteTurn);
    }

    public GameState previousState() {
        return moveHistory.getMoveCount() == 0 ? this : new GameState(chessBoard, moveHistory.withoutLastMove(), isWhiteTurn);
    }

    @Override
    public ChessPiece getPieceAt(Square square) {
        return chessBoard.getPieceAt(square);
    }

    @Override
    public Square getPiecePosition(ChessPiece chessPiece) {
        return chessBoard.getPiecePosition(chessPiece);
    }

    @Override
    public ChessBoardBounds getBounds() {
        return chessBoard.getBounds();
    }

    @Override
    public List<ChessPiece> getPiecesOfColour(boolean isWhite) {
        return chessBoard.getPiecesOfColour(isWhite);
    }

    @Override
    public List<ChessPiece> getPieces() {
        return chessBoard.getPieces();
    }

    @Override
    public List<ChessPiece> getPiecesOfTypeAndColour(PieceType pieceType, boolean isWhite) {
        return chessBoard.getPiecesOfTypeAndColour(pieceType, isWhite);
    }

    @Override
    public List<ChessPiece> getPiecesOfType(PieceType pieceType) {
        return chessBoard.getPiecesOfType(pieceType);
    }

    @Override
    public String toFen() {
        return chessBoard.toFen();
    }

    @Override
    public GameState withPieceAt(ChessPiece piece, Square square) {
        return withChessBoard(chessBoard.withPieceAt(piece, square));
    }

    @Override
    public ChessBoard withoutPieceAt(Square square) {
        return withChessBoard(chessBoard.withoutPieceAt(square));
    }

    @Override
    public ChessBoard withPieceMoved(Square from, Square to) {
        return withChessBoard(chessBoard.withPieceMoved(from, to));
    }

    @Override
    public ChessBoard withAllPieces(Map<Square, ChessPiece> pieces) {
        return withChessBoard(chessBoard.withAllPieces(pieces));
    }

    @Override
    public ChessBoard withoutPieces() {
        return withChessBoard(chessBoard.withoutPieces());
    }

    @Override
    public ChessBoard withBounds(ChessBoardBounds bounds) {
        return withChessBoard(chessBoard.withBounds(bounds));
    }
}