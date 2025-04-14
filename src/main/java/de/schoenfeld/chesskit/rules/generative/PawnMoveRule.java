package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.board.ChessBoardBounds;
import de.schoenfeld.chesskit.board.SquareChessBoardBounds;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.*;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.CaptureComponent;
import de.schoenfeld.chesskit.move.components.PromotionComponent;

import java.util.List;

public class PawnMoveRule<P extends PieceType> extends AbstractGenerativeMoveRule<Square8x8, P> {
    private static final PawnMoveRule<StandardPieceType> STANDARD =
            new PawnMoveRule<>(StandardPieceType.PAWN,
                    List.of(StandardPieceType.QUEEN, StandardPieceType.ROOK,
                            StandardPieceType.BISHOP, StandardPieceType.KNIGHT));
    private final List<P> promotionTypes;

    public PawnMoveRule(P pawnType, List<P> promotionTypes) {
        super(pawnType);
        if (promotionTypes == null) throw new NullPointerException("promotionTypes");
        this.promotionTypes = List.copyOf(promotionTypes);
    }

    public static PawnMoveRule<StandardPieceType> standard() {
        return STANDARD;
    }

    private static <P extends PieceType> boolean isPromotionRank(ChessPiece<P> pawn,
                                                                 ChessBoardBounds<Square8x8> boardBounds,
                                                                 int rank) {
        // FIXME: This is obviously not scalable nor is it clean
        // TODO: Fix this abomination
        SquareChessBoardBounds uglyExplicitCast = (SquareChessBoardBounds) (ChessBoardBounds) boardBounds;
        int endRank = pawn.color() == Color.WHITE ? uglyExplicitCast.rows() - 1 : 0;
        return rank == endRank;
    }

    @Override
    protected void generatePieceMoves(GameState<Square8x8, P> gameState,
                                      Square8x8 pawnSquare,
                                      MoveLookup<Square8x8, P> moves) {
        int direction = gameState.getColor() == Color.WHITE ? 1 : -1;
        ChessPiece<P> pawn = gameState.getPieceAt(pawnSquare);
        // Add one-step move
        Square8x8 oneForward = pawnSquare.offset(0, direction);
        // Check if the one-step move is possible
        if (gameState.getBounds().contains(oneForward) &&
                gameState.getPieceAt(oneForward) == null) {
            // Check if the one-step move is a promotion
            if (isPromotionRank(pawn, gameState.getBounds(), oneForward.y()))
                for (P pieceType : promotionTypes)
                    moves.add(Move.of(pawn, pawnSquare, oneForward, new PromotionComponent<>(pieceType)));
                // Otherwise, add a normal one-step move
            else moves.add(Move.of(pawn, pawnSquare, oneForward));

            int advanceRank = pawn.color() == Color.WHITE ? 1 : 6;

            // Add a two-step move if pawn hasn't moved yet
            if (pawnSquare.y() == advanceRank) {
                Square8x8 twoForward = oneForward.offset(0, direction);
                // Check if the two-step move is possible
                if (gameState.getBounds().contains(twoForward) &&
                        gameState.getPieceAt(twoForward) == null) {
                    // Check if the two-step move is a promotion
                    if (isPromotionRank(pawn, gameState.getBounds(), twoForward.y()))
                        for (P pieceType : promotionTypes)
                            moves.add(Move.of(pawn, pawnSquare, twoForward, new PromotionComponent<>(pieceType)));
                        // Otherwise, add a normal two-step move
                    else moves.add(Move.of(pawn, pawnSquare, twoForward));
                }
            }
        }
        // Add capture moves
        int[] captureDirections = {1, -1};
        for (int captureDirection : captureDirections) {
            var capturePosition = pawnSquare.offset(captureDirection, direction);
            // Check if the capture is on the board
            if (gameState.getBounds().contains(capturePosition)) {
                var capturePiece = gameState.getPieceAt(capturePosition);
                // Check if the capture is possible
                if (capturePiece != null && capturePiece.color() != pawn.color()) {
                    // Check if the capture is a promotion
                    if (isPromotionRank(pawn, gameState.getBounds(), capturePosition.y()))
                        for (P pieceType : promotionTypes)
                            moves.add(Move.of(pawn, pawnSquare, capturePosition, new PromotionComponent<>(pieceType), new CaptureComponent<>(capturePiece)));
                        // Otherwise, add a normal capture
                    else moves.add(Move.of(pawn, pawnSquare, capturePosition, new CaptureComponent<>(capturePiece)));
                }
            }
        }
    }
}
