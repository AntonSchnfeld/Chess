package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.model.*;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.CaptureComponent;
import de.schoenfeld.chesskit.move.components.PromotionComponent;

import java.util.List;

public class PawnMoveRule<T extends PieceType> extends AbstractGenerativeMoveRule<T> {
    private static final PawnMoveRule<StandardPieceType> STANDARD =
            new PawnMoveRule<>(StandardPieceType.PAWN,
                    List.of(StandardPieceType.QUEEN, StandardPieceType.ROOK,
                            StandardPieceType.BISHOP, StandardPieceType.KNIGHT));
    private final List<T> promotionTypes;

    public PawnMoveRule(T pawnType, List<T> promotionTypes) {
        super(pawnType);
        if (promotionTypes == null) throw new NullPointerException("promotionTypes");
        this.promotionTypes = List.copyOf(promotionTypes);
    }

    public static PawnMoveRule<StandardPieceType> standard() {
        return STANDARD;
    }

    private static <T extends PieceType> boolean isPromotionRank(ChessPiece<T> pawn,
                                                                 int rank,
                                                                 ChessBoardBounds bounds) {
        int endRank = pawn.isWhite() ? bounds.rows() - 1 : 0;
        return rank == endRank;
    }

    @Override
    protected void generatePieceMoves(GameState<T> gameState,
                                      Square pawnSquare,
                                      MoveLookup<T> moves) {
        int direction = gameState.isWhiteTurn() ? 1 : -1;
        ChessPiece<T> pawn = gameState.getPieceAt(pawnSquare);
        // Add one-step move
        Square oneForward = pawnSquare.offset(0, direction);
        // Check if the one-step move is possible
        if (gameState.getBounds().contains(oneForward) &&
                gameState.getPieceAt(oneForward) == null) {
            // Check if the one-step move is a promotion
            if (isPromotionRank(pawn, oneForward.y(), gameState.getBounds()))
                for (T pieceType : promotionTypes)
                    moves.add(Move.claim(pawn, pawnSquare, oneForward, new PromotionComponent<>(pieceType)));
                // Otherwise, add a normal one-step move
            else moves.add(Move.claim(pawn, pawnSquare, oneForward));

            int advanceRank = pawn.isWhite() ? 1 : 6;

            // Add a two-step move if pawn hasn't moved yet
            if (pawnSquare.y() == advanceRank) {
                Square twoForward = oneForward.offset(0, direction);
                // Check if the two-step move is possible
                if (gameState.getBounds().contains(twoForward) &&
                        gameState.getPieceAt(twoForward) == null) {
                    // Check if the two-step move is a promotion
                    if (isPromotionRank(pawn, twoForward.y(), gameState.getBounds()))
                        for (T pieceType : promotionTypes)
                            moves.add(Move.claim(pawn, pawnSquare, twoForward, new PromotionComponent<>(pieceType)));
                        // Otherwise, add a normal two-step move
                    else moves.add(Move.claim(pawn, pawnSquare, twoForward));
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
                if (capturePiece != null && capturePiece.isWhite() != pawn.isWhite()) {
                    // Check if the capture is a promotion
                    if (isPromotionRank(pawn, capturePosition.y(), gameState.getBounds()))
                        for (T pieceType : promotionTypes)
                            moves.add(Move.claim(pawn, pawnSquare, capturePosition, new PromotionComponent<>(pieceType), new CaptureComponent<>(capturePiece)));
                        // Otherwise, add a normal capture
                    else moves.add(Move.claim(pawn, pawnSquare, capturePosition, new CaptureComponent<>(capturePiece)));
                }
            }
        }
    }
}
