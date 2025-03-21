package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.Move;

public record PromotionComponent(PieceType promotionTo) implements MoveComponent {
    @Override
    public ChessBoard executeOn(GameState gameState, Move move) {
        ChessPiece promotedPiece = new ChessPiece(promotionTo, move.movedPiece().isWhite());

        return gameState.chessBoard()
                .withoutPieceAt(move.from())
                .withPieceAt(promotedPiece, move.to());
    }
}
