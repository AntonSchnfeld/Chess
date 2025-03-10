package de.schoenfeld.chess.move.handlers;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.components.CastlingComponent;

public class CastlingComponentHandler implements MoveComponentHandler<CastlingComponent> {
    @Override
    public GameState handle(GameState gameState, CastlingComponent component, Move move) {
        Move rookMove = component.rookMove();

        return gameState.withChessBoard(gameState.chessBoard()
                .withPieceMoved(rookMove.from(), rookMove.to()));
    }
}
