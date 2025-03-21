package de.schoenfeld.chess.rules.restrictive;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;

import java.util.Iterator;

public class FriendlyFireRule implements RestrictiveMoveRule {
    @Override
    public void filterMoves(MoveCollection moves, GameState gameState) {
        Iterator<Move> iterator = moves.iterator();

        while (iterator.hasNext()) { // Properly iterating over the collection
            Move move = iterator.next();
            ChessPiece targetPiece = gameState.chessBoard().getPieceAt(move.to());

            if (targetPiece != null && move.movedPiece().isWhite() == targetPiece.isWhite()) {
                iterator.remove();
            }
        }
    }
}
