package de.schoenfeld.chess.model;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.board.MapChessBoard;

import java.io.Serializable;

public record GameState(
        ImmutableChessBoard chessBoard,
        MoveHistory moveHistory,
        boolean isWhiteTurn
) implements Serializable {

    // Compact constructor for validation
    public GameState {
        java.util.Objects.requireNonNull(chessBoard, "chessBoard cannot be null");
        java.util.Objects.requireNonNull(moveHistory, "moveHistory cannot be null");
    }

    public GameState(ImmutableChessBoard chessBoard, MoveHistory moveHistory) {
        this(chessBoard, moveHistory, true);
    }

    public GameState() {
        this(new MapChessBoard(new ChessBoardBounds(8, 8)),
                new MoveHistory(), true);
    }

    // Factory method for initial state
    public static GameState createInitial() {
        return new GameState(
                new MapChessBoard(new ChessBoardBounds(8, 8)),
                new MoveHistory(),
                true
        );
    }

    // Derived turn state - no need to store separately
    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public GameState withIsWhiteTurn(boolean isWhiteTurn) {
        return new GameState(
                chessBoard,
                moveHistory,
                isWhiteTurn
        );
    }

    // State transition methods
    public GameState withChessBoard(ImmutableChessBoard newBoard) {
        return new GameState(newBoard, moveHistory, isWhiteTurn);
    }

    public GameState withMoveHistory(MoveHistory newHistory) {
        return new GameState(chessBoard, newHistory, isWhiteTurn);
    }

    // Previous board state access (for undo)
    public GameState previousState() {
        return new GameState(
                chessBoard,
                moveHistory.withoutLastMove(),
                isWhiteTurn
        );
    }
}