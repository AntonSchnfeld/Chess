package de.schoenfeld.chess.logic.rule;

import de.schoenfeld.chess.data.ReadOnlyGameState;
import de.schoenfeld.chess.data.move.MoveCollection;
import de.schoenfeld.chess.logic.piece.ChessPiece;
import de.schoenfeld.chess.logic.piece.PieceType;

import java.util.List;

public class PromotionRule implements MoveRule {
    @Override
    public void apply(MoveCollection moves, ReadOnlyGameState gameState) {
        List<ChessPiece> promotionCandidates = gameState
                .getChessBoard()
                .getPiecesOfType(PieceType.PAWN, gameState.isWhiteTurn());

        int endRank = (gameState.isWhiteTurn()) ? 7 : 0;

        for (ChessPiece pawn : promotionCandidates) {
            if (gameState.getChessBoard().getPiecePosition(pawn).y() == endRank) {
                // Get all moves of that pawn in MoveCollection and replace them with promotion moves
                // Somehow handle promotion properly
            }
        }
    }
}
