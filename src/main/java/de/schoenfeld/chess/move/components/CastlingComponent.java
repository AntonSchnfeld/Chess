package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.Move;

public record CastlingComponent(Move rookMove) implements MoveComponent {

    public CastlingComponent(ChessPiece rook, Position from, Position to) {
        this(Move.of(rook, from, to));
    }

    @Override
    public ImmutableChessBoard executeOn(GameState gameState, Move move) {
        return gameState.chessBoard().withPieceMoved(rookMove.from(), rookMove.to());
    }
}
