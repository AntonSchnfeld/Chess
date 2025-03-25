package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.Move;

public record PromotionComponent<T extends PieceType>(T promotionTo)
        implements MoveComponent<T> {
    @Override
    public void executeOn(GameState<T> gameState,
                          Move<T> move) {
        ChessPiece<T> promotedPiece = new ChessPiece<>(promotionTo, move.movedPiece().isWhite());

        gameState.removePieceAt(move.from());
        gameState.setPieceAt(move.to(), promotedPiece);
    }

    @Override
    public void undoOn(GameState<T> gameState,
                       Move<T> move) {
        gameState.removePieceAt(move.to());
        gameState.setPieceAt(move.from(), move.movedPiece());
    }
}
