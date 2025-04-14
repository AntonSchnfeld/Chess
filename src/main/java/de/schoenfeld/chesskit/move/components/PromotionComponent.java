package de.schoenfeld.chesskit.move.components;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;

public record PromotionComponent<T extends Tile, P extends PieceType>(P promotionTo)
        implements MoveComponent<T, P> {
    @Override
    public void makeOn(GameState<T, P> gameState,
                       Move<T, P> move) {
        ChessPiece<P> promotedPiece = new ChessPiece<>(promotionTo, move.movedPiece().color());

        gameState.removePieceAt(move.from());
        gameState.setPieceAt(move.to(), promotedPiece);
    }

    @Override
    public void unmakeOn(GameState<T, P> gameState,
                         Move<T, P> move) {
        gameState.removePieceAt(move.to());
        gameState.setPieceAt(move.from(), move.movedPiece());
    }
}
