package de.schoenfeld.chesskit.move.components;

import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;

public record CaptureComponent<T extends PieceType>(ChessPiece<T> capturedPiece)
        implements MoveComponent<T> {
    @Override
    public void executeOn(GameState<T> gameState, Move<T> move) {
    }

    @Override
    public void undoOn(GameState<T> gameState, Move<T> move) {
        gameState.setPieceAt(move.to(), capturedPiece);
    }
}
