package de.schoenfeld.chess.data;

public interface ReadOnlyGameState {
    ReadOnlyChessBoard getChessBoard();

    MoveHistory getMoveHistory();

    boolean isWhiteTurn();
}
