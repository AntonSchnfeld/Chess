package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.model.*;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.CastlingComponent;

public class CastlingRule<P extends PieceType> implements GenerativeMoveRule<Square8x8, P> {
    private final Square8x8 whiteKingPosition;
    private final Square8x8 whiteQueenSideRookPosition;
    private final Square8x8 whiteKingSideRookPosition;

    private final Square8x8 blackKingPosition;
    private final Square8x8 blackQueenSideRookPosition;
    private final Square8x8 blackKingSideRookPosition;

    private final P kingType, rookType;

    private static final CastlingRule<StandardPieceType> STANDARD = new CastlingRule<>(
            Square8x8.of(4, 0), Square8x8.of(0, 0), Square8x8.of(7, 0),
            Square8x8.of(4, 7), Square8x8.of(0, 7), Square8x8.of(7, 7),
            StandardPieceType.KING, StandardPieceType.ROOK
    );

    public static CastlingRule<StandardPieceType> standard() {
        return STANDARD;
    }

    public CastlingRule(Square8x8 whiteKingPosition,
                        Square8x8 whiteQueenSideRookPosition,
                        Square8x8 whiteKingSideRookPosition,
                        Square8x8 blackKingPosition,
                        Square8x8 blackQueenSideRookPosition,
                        Square8x8 blackKingSideRookPosition,
                        P kingType,
                        P rookType) {
        this.whiteKingPosition = whiteKingPosition;
        this.whiteQueenSideRookPosition = whiteQueenSideRookPosition;
        this.whiteKingSideRookPosition = whiteKingSideRookPosition;
        this.blackKingPosition = blackKingPosition;
        this.blackQueenSideRookPosition = blackQueenSideRookPosition;
        this.blackKingSideRookPosition = blackKingSideRookPosition;
        this.kingType = kingType;
        this.rookType = rookType;
    }

    @Override
    public void generateMoves(GameState<Square8x8, P> gameState,
                              MoveLookup<Square8x8, P> moves) {
        boolean isBlack = gameState.getColor() == Color.BLACK;
        Square8x8 kingPositionForCastling = isBlack ? blackKingPosition : whiteKingPosition;
        Square8x8 queenSideRookPosition = isBlack ? blackQueenSideRookPosition : whiteQueenSideRookPosition;
        Square8x8 kingSideRookPosition = isBlack ? blackKingSideRookPosition : whiteKingSideRookPosition;

        ChessPiece<P> king = gameState.getPieceAt(kingPositionForCastling);
        if (king == null || king.pieceType() != kingType
                || king.color() != gameState.getColor())
            return;

        checkAndAddCastlingMove(gameState, kingPositionForCastling, queenSideRookPosition, moves);
        checkAndAddCastlingMove(gameState, kingPositionForCastling, kingSideRookPosition, moves);
    }

    private void checkAndAddCastlingMove(GameState<Square8x8, P> gameState,
                                         Square8x8 kingPosition,
                                         Square8x8 rookPosition,
                                         MoveLookup<Square8x8, P> moves) {
        ChessPiece<P> rook = gameState.getPieceAt(rookPosition);
        if (rook == null || rook.pieceType() != rookType
                || rook.color() != gameState.getColor()) return;

        int xDirection = Integer.compare(rookPosition.x(), kingPosition.x());
        int y = kingPosition.y();

        for (int x = kingPosition.x() + xDirection; x != rookPosition.x(); x += xDirection) {
            ChessPiece<P> squarePiece = gameState.getPieceAt(Square8x8.of(x, y));
            if (squarePiece != null) return;
        }

        Square8x8 targetKingPosition = Square8x8.of(kingPosition.x() + (2 * xDirection), y);
        Move<Square8x8, P> castlingMove = Move.of(
                gameState.getPieceAt(kingPosition),
                kingPosition,
                targetKingPosition,
                new CastlingComponent<>(rookPosition, targetKingPosition.offset(-xDirection, 0))
        );
        moves.add(castlingMove);
    }
}
