package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.Move;

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
