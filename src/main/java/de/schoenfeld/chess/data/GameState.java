package de.schoenfeld.chess.data;

import de.schoenfeld.chess.ChessBoardBounds;

public class GameState implements ReadOnlyGameState {
    private ChessBoard chessBoard;
    private MoveHistory moveHistory;
    private boolean whiteTurn;

    public GameState(ChessBoard chessBoard, MoveHistory moveHistory) {
        this.chessBoard = chessBoard;
        this.moveHistory = moveHistory;
        whiteTurn = true;
    }

    public GameState() {
        this(new MapChessBoard(new ChessBoardBounds(8, 8)), new MoveHistory());
    }

    @Override
    public MoveHistory getMoveHistory() {
        return moveHistory;
    }

    public void setMoveHistory(MoveHistory moveHistory) {
        this.moveHistory = moveHistory;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public void setWhiteTurn(boolean whiteTurn) {
        this.whiteTurn = whiteTurn;
    }

    @Override
    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }
}
