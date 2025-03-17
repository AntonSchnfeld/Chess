package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.model.*;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.PromotionComponent;

import java.util.List;

public class PawnMoveRule implements GenerativeMoveRule {
    private final List<PieceType> promotionTypes;

    public PawnMoveRule(List<PieceType> promotionTypes) {
        if (promotionTypes == null) throw new NullPointerException("promotionTypes");
        this.promotionTypes = List.copyOf(promotionTypes);
    }

    public PawnMoveRule() {
        this(List.of(PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT));
    }

    private static void generatePawnMoves(GameState gameState,
                                          ChessPiece pawn,
                                          MoveCollection moves,
                                          List<PieceType> promotionTypes) {
        int direction = gameState.isWhiteTurn() ? 1 : -1;
        var board = gameState.chessBoard();

        var from = board.getPiecePosition(pawn);

        // Add one-step move
        var oneForward = from.offset(0, direction);
        // Check if the one-step move is possible
        if (board.getBounds().contains(oneForward) && board.getPieceAt(oneForward) == null) {
            // Check if the one-step move is a promotion
            if (isPromotionRank(pawn, oneForward.y(), board.getBounds()))
                addPromotionMoves(moves, pawn, from, oneForward, promotionTypes);
                // Otherwise, add a normal one-step move
            else moves.add(Move.of(pawn, from, oneForward));

            // Add a two-step move if pawn hasn't moved yet
            if (!pawn.hasMoved()) {
                var twoForward = oneForward.offset(0, direction);
                // Check if the two-step move is possible
                if (board.getBounds().contains(twoForward) && board.getPieceAt(twoForward) == null) {
                    // Check if the two-step move is a promotion
                    if (isPromotionRank(pawn, twoForward.y(), board.getBounds()))
                        addPromotionMoves(moves, pawn, from, twoForward, promotionTypes);
                        // Otherwise, add a normal two-step move
                    else moves.add(Move.of(pawn, from, twoForward));
                }
            }
        }
        // Add capture moves
        int[] captureDirections = {1, -1};
        for (int captureDirection : captureDirections) {
            var capturePosition = from.offset(captureDirection, direction);
            // Check if the capture is on the board
            if (board.getBounds().contains(capturePosition)) {
                var capturePiece = board.getPieceAt(capturePosition);
                // Check if the capture is possible
                if (capturePiece != null && capturePiece.isWhite() != pawn.isWhite()) {
                    // Check if the capture is a promotion
                    if (isPromotionRank(pawn, capturePosition.y(), board.getBounds()))
                        addPromotionMoves(moves, pawn, from, capturePosition, promotionTypes);
                        // Otherwise, add a normal capture
                    else moves.add(Move.of(pawn, from, capturePosition));
                }
            }
        }
    }

    private static void addPromotionMoves(MoveCollection moves,
                                          ChessPiece pawn,
                                          Position from,
                                          Position to,
                                          List<PieceType> promotionTypes) {
        for (PieceType promotionType : promotionTypes)
            moves.add(Move.of(pawn, from, to, new PromotionComponent(promotionType)));
    }

    private static boolean isPromotionRank(ChessPiece pawn,
                                           int rank,
                                           ChessBoardBounds bounds) {
        int endRank = pawn.isWhite() ? bounds.rows() - 1 : 0;
        return rank == endRank;
    }

    @Override
    public MoveCollection generateMoves(GameState gameState) {
        var moves = new MoveCollection();

        var pawns = gameState
                .chessBoard()
                .getPiecesOfType(PieceType.PAWN, gameState.isWhiteTurn());

        for (var pawn : pawns) {
            generatePawnMoves(gameState, pawn, moves, promotionTypes);
        }

        return moves;
    }
}
