package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.Move;

public record CaptureComponent(ChessPiece capturedPiece) implements MoveComponent {
    @Override
    public <T extends PieceType> ChessBoard<T> executeOn(GameState<T> gameState,
                                                         Move move) {
        return gameState.chessBoard();
    }
}
