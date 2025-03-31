package de.schoenfeld.chesskit.move.components;

import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;

public record PromotionComponent<T extends PieceType>(T promotionTo)
        implements MoveComponent<T> {
    @Override
    public void makeOn(GameState<T> gameState,
                       Move<T> move) {
        ChessPiece<T> promotedPiece = new ChessPiece<>(promotionTo, move.movedPiece().isWhite());

        gameState.removePieceAt(move.from());
        gameState.setPieceAt(move.to(), promotedPiece);
    }

    @Override
    public void unmakeOn(GameState<T> gameState,
                         Move<T> move) {
        gameState.removePieceAt(move.to());
        gameState.setPieceAt(move.from(), move.movedPiece());
    }
}
