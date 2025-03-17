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
        if (!iterator.hasNext()) return;

        for (Move move = iterator.next(); iterator.hasNext(); move = iterator.next()) {
            ChessPiece targetPiece = gameState.chessBoard().getPieceAt(move.to());
            if (targetPiece == null) continue;

            if (move.movedPiece().isWhite() == targetPiece.isWhite())
                iterator.remove();
        }
    }
}
