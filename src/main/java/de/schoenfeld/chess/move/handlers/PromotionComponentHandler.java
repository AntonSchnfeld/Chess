package de.schoenfeld.chess.move.handlers;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.components.PromotionComponent;

public class PromotionComponentHandler implements MoveComponentHandler<PromotionComponent> {
    @Override
    public GameState handle(GameState gameState, PromotionComponent component, Move move) {
        ChessPiece promotedPiece = new ChessPiece(component.promotionTo(), move.movedPiece().isWhite());

        return gameState.withChessBoard(gameState.chessBoard()
                .withoutPieceAt(move.from())
                .withPieceAt(promotedPiece, move.to()));
    }
}