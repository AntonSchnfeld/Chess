package de.schoenfeld.chesskit.rules.restrictive;

import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;

public class FriendlyFireRule<T extends Tile, P extends PieceType> implements RestrictiveMoveRule<T, P> {
    private static final FriendlyFireRule<Square8x8, StandardPieceType> STANDARD = new FriendlyFireRule<>();

    public static FriendlyFireRule<Square8x8, StandardPieceType> standard() {
        return STANDARD;
    }

    @Override
    public void filterMoves(MoveLookup<T, P> moves, GameState<T, P> gameState) {
        for (int i = 0; i < moves.size(); i++) { // Properly iterating over the collection
            Move<T, P> move = moves.get(i);
            ChessPiece<P> targetPiece = gameState.getChessBoard().getPieceAt(move.to());

            if (targetPiece != null && move.movedPiece().color() == targetPiece.color()) {
                moves.remove(i);
                i--;
            }
        }
    }
}
