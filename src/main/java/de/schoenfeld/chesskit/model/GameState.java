package de.schoenfeld.chesskit.model;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.board.ChessBoardBounds;
import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.MoveComponent;
import de.schoenfeld.chesskit.rules.MoveGenerator;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameState<T extends Tile, P extends PieceType> implements ChessBoard<T, P> {
    @Serial
    private static final long serialVersionUID = 4583658841385236346L;

    private ChessBoard<T, P> chessBoard;
    private MoveHistory<T, P> moveHistory;
    private MoveLookup<T, P> moves;
    private MoveLookup<T, P> pseudoLegalMoves;
    private MoveGenerator<T, P> moveGenerator;
    private Color turnColor;
    private boolean pseudoLegalMovesDirty, movesDirty;

    public GameState(ChessBoard<T, P> chessBoard, MoveHistory<T, P> moveHistory,
                     MoveGenerator<T, P> moveGenerator, Color turnColor) {
        if (chessBoard == null)
            throw new NullPointerException("chessBoard");
        if (moveHistory == null)
            throw new NullPointerException("moveHistory");
        if (moveGenerator == null)
            throw new NullPointerException("moveGenerator");
        this.chessBoard = chessBoard;
        this.moveHistory = moveHistory;
        this.turnColor = turnColor;
        this.moveGenerator = moveGenerator;
        this.pseudoLegalMoves = new MoveLookup<>();
        moveGenerator.generatePseudoLegalMoves(this, pseudoLegalMoves);
        this.moves = new MoveLookup<>();
        moveGenerator.generateMoves(this, moves);
        this.pseudoLegalMovesDirty = false;
        this.movesDirty = false;
    }

    public GameState(ChessBoard<T, P> chessBoard, MoveHistory<T, P> moveHistory, MoveGenerator<T, P> moveGenerator) {
        this(chessBoard, moveHistory, moveGenerator, Color.WHITE);
    }

    public GameState(ChessBoard<T, P> chessBoard, MoveGenerator<T, P> moveGenerator) {
        this(chessBoard, new MoveHistory<>(), moveGenerator, Color.WHITE);
    }

    public ChessBoard<T, P> getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(ChessBoard<T, P> newBoard) {
        this.chessBoard = newBoard;
        invalidateMoves();
    }

    private void invalidateMoves() {
        pseudoLegalMovesDirty = true;
        movesDirty = true;
    }

    public MoveGenerator<T, P> getMoveGenerator() {
        return moveGenerator;
    }

    public void setMoveGenerator(MoveGenerator<T, P> newGenerator) {
        this.moveGenerator = newGenerator;
        invalidateMoves();
    }

    public MoveHistory<T, P> getMoveHistory() {
        return moveHistory;
    }

    public void setMoveHistory(MoveHistory<T, P> newHistory) {
        this.moveHistory = newHistory;
        invalidateMoves();
    }

    public Color getColor() {
        return turnColor;
    }

    public void makeMove(Move<T, P> move) {
        moveHistory.recordMove(move);
        List<MoveComponent<T, P>> components = move.getComponents();
        for (MoveComponent<T, P> component : components)
            component.makeOn(this, move);
        chessBoard.movePiece(move.from(), move.to());
        switchTurn();
        invalidateMoves();
    }

    public void unmakeLastMove() {
        Move<T, P> lastMove = moveHistory.getLastMove();
        switchTurn();
        chessBoard.movePiece(lastMove.to(), lastMove.from());
        List<MoveComponent<T, P>> components = lastMove.getComponents();
        for (MoveComponent<T, P> component : components)
            component.unmakeOn(this, lastMove);
        moveHistory.removeLastMove();
        invalidateMoves();
    }

    public MoveLookup<T, P> getMoves() {
        updateMovesIfNecessary();
        return moves;
    }

    public MoveLookup<T, P> getPseudoLegalMoves() {
        updateMovesIfNecessary();
        return pseudoLegalMoves;
    }

    private void updateMovesIfNecessary() {
        if (pseudoLegalMovesDirty) {
            pseudoLegalMoves = new MoveLookup<>();
            moveGenerator.generatePseudoLegalMoves(this, pseudoLegalMoves);
            pseudoLegalMovesDirty = false;
        }
        if (movesDirty) {
            moves = new MoveLookup<>();
            moveGenerator.generateMoves(this, moves);
            movesDirty = false;
        }
    }

    // State transition methods optimized for performance
    public void setIsWhiteTurn(Color color) {
        this.turnColor = color;
        invalidateMoves();
    }

    public void switchTurn() {
        turnColor = turnColor.opposite();
        invalidateMoves();
    }

    @Override
    public ChessPiece<P> getPieceAt(T tile) {
        return chessBoard.getPieceAt(tile);
    }

    @Override
    public ChessBoardBounds<T> getBounds() {
        return chessBoard.getBounds();
    }

    @Override
    public void setBounds(ChessBoardBounds<T> bounds) {
        chessBoard.setBounds(bounds);
        invalidateMoves();
    }

    @Override
    public List<T> getTilesWithColour(Color color) {
        return chessBoard.getTilesWithColour(color);
    }

    @Override
    public List<T> getOccupiedTiles() {
        return chessBoard.getOccupiedTiles();
    }

    @Override
    public List<T> getTilesWithTypeAndColour(P pieceType, Color color) {
        return chessBoard.getTilesWithTypeAndColour(pieceType, color);
    }

    @Override
    public List<T> getTilesWithType(P pieceType) {
        return chessBoard.getTilesWithType(pieceType);
    }

    @Override
    public boolean isOccupied(T tile) {
        return chessBoard.isOccupied(tile);
    }

    @Override
    public void setPieceAt(T tile, ChessPiece<P> piece) {
        chessBoard.setPieceAt(tile, piece);
        invalidateMoves();
    }

    @Override
    public void removePieceAt(T tile) {
        chessBoard.removePieceAt(tile);
        invalidateMoves();
    }

    @Override
    public void movePiece(T from, T to) {
        chessBoard.movePiece(from, to);
        invalidateMoves();
    }

    @Override
    public void setAllPieces(Map<T, ChessPiece<P>> pieces) {
        chessBoard.setAllPieces(pieces);
        invalidateMoves();
    }

    @Override
    public void removePieces() {
        chessBoard.removePieces();
        invalidateMoves();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        GameState<?, ?> gameState = (GameState<?, ?>) o;
        return pseudoLegalMovesDirty == gameState.pseudoLegalMovesDirty
                && movesDirty == gameState.movesDirty
                && Objects.equals(chessBoard, gameState.chessBoard)
                && Objects.equals(moveHistory, gameState.moveHistory)
                && Objects.equals(moves, gameState.moves)
                && Objects.equals(pseudoLegalMoves, gameState.pseudoLegalMoves)
                && Objects.equals(moveGenerator, gameState.moveGenerator)
                && turnColor == gameState.turnColor;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(chessBoard);
        result = 31 * result + Objects.hashCode(moveHistory);
        result = 31 * result + Objects.hashCode(moves);
        result = 31 * result + Objects.hashCode(pseudoLegalMoves);
        result = 31 * result + Objects.hashCode(moveGenerator);
        result = 31 * result + Objects.hashCode(turnColor);
        result = 31 * result + Boolean.hashCode(pseudoLegalMovesDirty);
        result = 31 * result + Boolean.hashCode(movesDirty);
        return result;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "chessBoard=" + chessBoard +
                ", moveHistory=" + moveHistory +
                ", moves=" + moves +
                ", pseudoLegalMoves=" + pseudoLegalMoves +
                ", moveGenerator=" + moveGenerator +
                ", turnColor=" + turnColor +
                ", pseudoLegalMovesDirty=" + pseudoLegalMovesDirty +
                ", movesDirty=" + movesDirty +
                '}';
    }
}