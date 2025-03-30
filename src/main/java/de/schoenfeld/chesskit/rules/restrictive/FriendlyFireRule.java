package de.schoenfeld.chesskit.rules.restrictive;

import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;

import java.util.Iterator;

public class FriendlyFireRule<T extends PieceType> implements RestrictiveMoveRule<T> {
    @Override
    public void filterMoves(MoveLookup<T> moves, GameState<T> gameState) {
        Iterator<Move<T>> iterator = moves.iterator();

        while (iterator.hasNext()) { // Properly iterating over the collection
            Move<T> move = iterator.next();
            ChessPiece<T> targetPiece = gameState.getChessBoard().getPieceAt(move.to());

            if (targetPiece != null && move.movedPiece().isWhite() == targetPiece.isWhite())
                iterator.remove();
        }
    }
}
