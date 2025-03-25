package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.model.*;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.PromotionComponent;

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

    private static <T extends PieceType> void addPromotionMoves(MoveCollection<T> moves,
                                                                ChessPiece<T> pawn,
                                                                Square from,
                                                                Square to,
                                                                List<T> promotionTypes) {
        for (T promotionType : promotionTypes)
            moves.add(Move.of(pawn, from, to, new PromotionComponent<>(promotionType)));
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
                                      MoveCollection<T> moves) {
        int direction = gameState.isWhiteTurn() ? 1 : -1;
        ChessPiece<T> pawn = gameState.getPieceAt(pawnSquare);
        // Add one-step move
        Square oneForward = pawnSquare.offset(0, direction);
        // Check if the one-step move is possible
        if (gameState.getBounds().contains(oneForward) &&
                gameState.getPieceAt(oneForward) == null) {
            // Check if the one-step move is a promotion
            if (isPromotionRank(pawn, oneForward.y(), gameState.getBounds()))
                addPromotionMoves(moves, pawn, pawnSquare, oneForward, promotionTypes);
                // Otherwise, add a normal one-step move
            else moves.add(Move.of(pawn, pawnSquare, oneForward));

            // Add a two-step move if pawn hasn't moved yet
            if (!pawn.hasMoved()) {
                Square twoForward = oneForward.offset(0, direction);
                // Check if the two-step move is possible
                if (gameState.getBounds().contains(twoForward) &&
                        gameState.getPieceAt(twoForward) == null) {
                    // Check if the two-step move is a promotion
                    if (isPromotionRank(pawn, twoForward.y(), gameState.getBounds()))
                        addPromotionMoves(moves, pawn, pawnSquare, twoForward, promotionTypes);
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
                if (capturePiece != null && capturePiece.isWhite() != pawn.isWhite()) {
                    // Check if the capture is a promotion
                    if (isPromotionRank(pawn, capturePosition.y(), gameState.getBounds()))
                        addPromotionMoves(moves, pawn, pawnSquare, capturePosition, promotionTypes);
                        // Otherwise, add a normal capture
                    else moves.add(Move.of(pawn, pawnSquare, capturePosition));
                }
            }
        }
    }
}
