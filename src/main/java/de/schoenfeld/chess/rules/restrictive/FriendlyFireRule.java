package de.schoenfeld.chess.rules.restrictive;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;

import java.util.Iterator;

public class FriendlyFireRule<T extends PieceType> implements RestrictiveMoveRule<T> {
    @Override
    public void filterMoves(MoveCollection<T> moves, GameState<T> gameState) {
        Iterator<Move<T>> iterator = moves.iterator();

        while (iterator.hasNext()) { // Properly iterating over the collection
            Move<T> move = iterator.next();
            ChessPiece<T> targetPiece = gameState.getChessBoard().getPieceAt(move.to());

            if (targetPiece != null && move.movedPiece().isWhite() == targetPiece.isWhite())
                iterator.remove();
        }
    }
}
