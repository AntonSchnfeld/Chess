package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.Move;

public record CaptureComponent(ChessPiece capturedPiece) implements MoveComponent {
    @Override
    public ImmutableChessBoard executeOn(GameState gameState, Move move) {
        return gameState.chessBoard();
    }
}
