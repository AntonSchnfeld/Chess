package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.Move;

public record PromotionComponent(StandardPieceType promotionTo)
        implements MoveComponent<StandardPieceType> {
    @Override
    public void executeOn(GameState<StandardPieceType> gameState,
                          Move<StandardPieceType> move) {
        ChessPiece<StandardPieceType> promotedPiece = new ChessPiece<>(promotionTo, move.movedPiece().isWhite());

        gameState.removePieceAt(move.from());
        gameState.setPieceAt(promotedPiece, move.to());
    }

    @Override
    public void undoOn(GameState<StandardPieceType> gameState,
                       Move<StandardPieceType> move) {
        gameState.removePieceAt(move.to());
        gameState.setPieceAt(move.movedPiece(), move.from());
    }
}
