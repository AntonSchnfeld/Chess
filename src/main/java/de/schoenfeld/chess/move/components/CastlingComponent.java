package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.Move;

public record CastlingComponent(Move<StandardPieceType> rookMove)
        implements MoveComponent<StandardPieceType> {

    public CastlingComponent(ChessPiece<StandardPieceType> rook, Square from, Square to) {
        this(Move.of(rook, from, to));
    }

    @Override
    public void executeOn(GameState<StandardPieceType> gameState,
                          Move<StandardPieceType> move) {
        gameState.movePiece(rookMove.from(), rookMove.to());
    }

    @Override
    public void undoOn(GameState<StandardPieceType> gameState,
                          Move<StandardPieceType> move) {
        gameState.movePiece(rookMove.to(), rookMove.from());
    }
}
