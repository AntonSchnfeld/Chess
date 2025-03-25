package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.Move;

public record CaptureComponent<T extends PieceType>(ChessPiece<T> capturedPiece)
        implements MoveComponent<T> {
    @Override
    public void executeOn(GameState<T> gameState, Move<T> move) {
        // Nothing needs to be done, is handled via normal movement
    }

    @Override
    public void undoOn(GameState<T> gameState, Move<T> move) {

    }
}
