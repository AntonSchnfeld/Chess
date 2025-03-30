package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.Square;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.CastlingComponent;

public class CastlingRule implements GenerativeMoveRule<StandardPieceType> {

    @Override
    public MoveLookup<StandardPieceType> generateMoves(GameState<StandardPieceType> gameState) {
        MoveLookup<StandardPieceType> moves = new MoveLookup<>();

        Square kingPositionForCastling = !gameState.isWhiteTurn() ?
                Square.of(4, 7) : Square.of(4, 0);
        Square queenSideRookPosition = !gameState.isWhiteTurn() ?
                Square.of(0, 7) : Square.of(0, 0);
        Square kingSideRookPosition = !gameState.isWhiteTurn() ?
                Square.of(7, 7) : Square.of(7, 0);

        ChessPiece<StandardPieceType> king = gameState.getPieceAt(kingPositionForCastling);
        if (king == null || king.pieceType() != StandardPieceType.KING
                || king.isWhite() != gameState.isWhiteTurn()) {
            return moves;
        }

        checkAndAddCastlingMove(gameState, kingPositionForCastling, queenSideRookPosition, moves);
        checkAndAddCastlingMove(gameState, kingPositionForCastling, kingSideRookPosition, moves);

        return moves;
    }

    private void checkAndAddCastlingMove(GameState<StandardPieceType> gameState,
                                         Square kingPosition,
                                         Square rookPosition,
                                         MoveLookup<StandardPieceType> moves) {
        ChessPiece<StandardPieceType> rook = gameState.getPieceAt(rookPosition);
        if (rook == null || rook.pieceType() != StandardPieceType.ROOK
                || rook.isWhite() != gameState.isWhiteTurn()) return;
        // The rook is of the correct color, is a rook and is at the correct position
        // Now we just check all squares between king and rook
        int xDirection = Integer.compare(rookPosition.x(), kingPosition.x());
        int y = kingPosition.y();

        for (int x = kingPosition.x() + xDirection; x != rookPosition.x(); x += xDirection) {
            ChessPiece<StandardPieceType> squarePiece = gameState.getPieceAt(Square.of(x, y));
            if (squarePiece != null) return;
        }

        Square targetKingPosition = Square.of(kingPosition.x() + (2 * xDirection), y);
        Move<StandardPieceType> castlingMove = Move.of(
                gameState.getPieceAt(kingPosition),
                kingPosition,
                targetKingPosition,
                new CastlingComponent<>(rookPosition, targetKingPosition.offset(-xDirection, 0))
        );
        moves.add(castlingMove);
    }
}