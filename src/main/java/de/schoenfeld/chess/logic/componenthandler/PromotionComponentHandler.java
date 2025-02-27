package de.schoenfeld.chess.logic.componenthandler;

import de.schoenfeld.chess.data.GameState;
import de.schoenfeld.chess.data.move.Move;
import de.schoenfeld.chess.data.move.PromotionComponent;
import de.schoenfeld.chess.logic.piece.ChessPiece;

public class PromotionComponentHandler implements MoveComponentHandler<PromotionComponent> {
    @Override
    public void handle(GameState gameState, PromotionComponent component, Move move) {
        gameState.getChessBoard().removePieceAt(move.from());
        ChessPiece promotedPiece = new ChessPiece(component.promotionTo(), move.movedPiece().isWhite());
        gameState.getChessBoard().setPiece(promotedPiece, move.to());
    }
}
