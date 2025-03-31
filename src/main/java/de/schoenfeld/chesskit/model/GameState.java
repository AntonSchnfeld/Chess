package de.schoenfeld.chesskit.model;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.board.MapChessBoard;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.components.MoveComponent;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameState<T extends PieceType> implements ChessBoard<T>, Serializable {
    @Serial
    private static final long serialVersionUID = 4583658841385236346L;

    private ChessBoard<T> chessBoard;
    private MoveHistory<T> moveHistory;
    private boolean isWhiteTurn;

    public GameState(ChessBoard<T> chessBoard, MoveHistory<T> moveHistory, boolean isWhiteTurn) {
        if (chessBoard == null)
            throw new NullPointerException("chessBoard");
        if (moveHistory == null)
            throw new NullPointerException("moveHistory");
        this.chessBoard = chessBoard;
        this.moveHistory = moveHistory;
        this.isWhiteTurn = isWhiteTurn;
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

    public ChessBoard<T> getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(ChessBoard<T> newBoard) {
        this.chessBoard = newBoard;
    }

    public MoveHistory<T> getMoveHistory() {
        return moveHistory;
    }

    public void setMoveHistory(MoveHistory<T> newHistory) {
        this.moveHistory = newHistory;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    @Override
    public boolean isOccupied(Square square) {
        return chessBoard.isOccupied(square);
    }

    public void makeMove(Move<T> move) {
        moveHistory.recordMove(move);
        final MoveComponent<T>[] components = move.getComponents();
        for (int i = 0; i < components.length; i++)
            components[i].makeOn(this, move);
        chessBoard.movePiece(move.from(), move.to());
        switchTurn();
    }

    public void unmakeLastMove() {
        Move<T> lastMove = moveHistory.getLastMove();
        switchTurn();
        chessBoard.movePiece(lastMove.to(), lastMove.from());
        final MoveComponent<T>[] components = lastMove.getComponents();
        for (int i = 0; i < components.length; i++)
            components[i].unmakeOn(this, lastMove);
        moveHistory.removeLastMove();
    }

    // State transition methods optimized for performance
    public void setIsWhiteTurn(boolean newTurn) {
        this.isWhiteTurn = newTurn;
    }

    public void switchTurn() {
        isWhiteTurn = !isWhiteTurn;
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
    public void setBounds(ChessBoardBounds bounds) {
        chessBoard.setBounds(bounds);
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
    public void setPieceAt(Square square, ChessPiece<T> piece) {
        chessBoard.setPieceAt(square, piece);
    }

    @Override
    public void removePieceAt(Square square) {
        chessBoard.removePieceAt(square);
    }

    @Override
    public void movePiece(Square from, Square to) {
        chessBoard.movePiece(from, to);
    }

    @Override
    public void setAllPieces(Map<Square, ChessPiece<T>> pieces) {
        chessBoard.setAllPieces(pieces);
    }

    @Override
    public void removePieces() {
        chessBoard.removePieces();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        GameState<?> gameState = (GameState<?>) object;
        return isWhiteTurn == gameState.isWhiteTurn && Objects.equals(chessBoard, gameState.chessBoard) && Objects.equals(moveHistory, gameState.moveHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, moveHistory, isWhiteTurn);
    }

    @Override
    public String toString() {
        return "GameState{" +
                "chessBoard=" + chessBoard +
                ", moveHistory=" + moveHistory +
                ", isWhiteTurn=" + isWhiteTurn +
                '}';
    }
}