package de.schoenfeld.chesskit.model;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.board.MapChessBoard;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.MoveComponent;
import de.schoenfeld.chesskit.rules.MoveGenerator;

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
    private MoveLookup<T> validMoves;
    private MoveGenerator<T> moveGenerator;
    private boolean isWhiteTurn;

    public GameState(ChessBoard<T> chessBoard, MoveHistory<T> moveHistory,
                     MoveGenerator<T> moveGenerator, boolean isWhiteTurn) {
        if (chessBoard == null)
            throw new NullPointerException("chessBoard");
        if (moveHistory == null)
            throw new NullPointerException("moveHistory");
        if (moveGenerator == null)
            throw new NullPointerException("moveGenerator");
        this.chessBoard = chessBoard;
        this.moveHistory = moveHistory;
        this.isWhiteTurn = isWhiteTurn;
        this.moveGenerator = moveGenerator;
        this.validMoves = moveGenerator.generateMoves(this);
    }

    public GameState(ChessBoard<T> chessBoard, MoveHistory<T> moveHistory, MoveGenerator<T> moveGenerator) {
        this(chessBoard, moveHistory, moveGenerator, true);
    }

    public GameState(ChessBoard<T> chessBoard, MoveGenerator<T> moveGenerator) {
        this(chessBoard, new MoveHistory<>(), moveGenerator, true);
    }

    public GameState(MoveGenerator<T> moveGenerator) {
        this(new MapChessBoard<>(), new MoveHistory<>(), moveGenerator, true);
    }

    public ChessBoard<T> getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(ChessBoard<T> newBoard) {
        this.chessBoard = newBoard;
    }

    public MoveGenerator<T> getMoveGenerator() {
        return moveGenerator;
    }

    public void setMoveGenerator(MoveGenerator<T> newGenerator) {
        this.moveGenerator = newGenerator;
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
        List<MoveComponent<T>> components = move.getComponents();
        for (MoveComponent<T> component : components)
            component.makeOn(this, move);
        chessBoard.movePiece(move.from(), move.to());
        switchTurn();
        updateValidMoves();
    }

    public void unmakeLastMove() {
        Move<T> lastMove = moveHistory.getLastMove();
        switchTurn();
        chessBoard.movePiece(lastMove.to(), lastMove.from());
        List<MoveComponent<T>> components = lastMove.getComponents();
        for (MoveComponent<T> component : components)
            component.unmakeOn(this, lastMove);
        moveHistory.removeLastMove();
        updateValidMoves();
    }

    public MoveLookup<T> getValidMoves() {
        return validMoves;
    }

    public void updateValidMoves() {
        this.validMoves = moveGenerator.generateMoves(this);
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