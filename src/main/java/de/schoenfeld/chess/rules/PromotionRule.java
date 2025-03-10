package de.schoenfeld.chess.rules;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.MoveCollection;

import java.util.List;

public class PromotionRule implements SpecialMoveRule {
    @Override
    public void apply(MoveCollection moves, GameState gameState) {
        List<ChessPiece> promotionCandidates = gameState
                .chessBoard()
                .getPiecesOfType(PieceType.PAWN, gameState.isWhiteTurn());

        int endRank = (gameState.isWhiteTurn()) ? 7 : 0;

        for (ChessPiece pawn : promotionCandidates) {
            if (gameState.chessBoard().getPiecePosition(pawn).y() == endRank) {
                // TODO
                // Get all moves of that pawn in MoveCollection and replace them with promotion moves
                // Somehow handle promotion properly
            }
        }
    }
}
