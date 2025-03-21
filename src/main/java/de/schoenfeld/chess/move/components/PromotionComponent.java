package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.Move;

public record PromotionComponent(StandardPieceType promotionTo)
        implements MoveComponent<StandardPieceType> {
    @Override
    public ChessBoard<StandardPieceType> executeOn(GameState<StandardPieceType> gameState,
                                                   Move<StandardPieceType> move) {
        ChessPiece<StandardPieceType> promotedPiece = new ChessPiece<>(promotionTo, move.movedPiece().isWhite());

        return gameState.chessBoard()
                .withoutPieceAt(move.from())
                .withPieceAt(promotedPiece, move.to());
    }
}
