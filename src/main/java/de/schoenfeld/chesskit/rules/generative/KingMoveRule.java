package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.*;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.CaptureComponent;

import java.util.List;

public class KingMoveRule<P extends PieceType> extends AbstractGenerativeMoveRule<Square8x8, P> {
    private static final KingMoveRule<StandardPieceType> STANDARD =
            new KingMoveRule<>(StandardPieceType.KING);
    private static final List<Square8x8> KING_DIRECTIONS = List.of(
            Square8x8.of(1, 0), Square8x8.of(-1, 0), Square8x8.of(0, 1), Square8x8.of(0, -1),
            Square8x8.of(1, 1), Square8x8.of(-1, -1), Square8x8.of(1, -1), Square8x8.of(-1, 1)
    );

    public KingMoveRule(P kingType) {
        super(kingType);
    }

    public static KingMoveRule<StandardPieceType> standard() {
        return STANDARD;
    }

    @Override
    protected void generatePieceMoves(GameState<Square8x8, P> gameState,
                                      Square8x8 kingPos,
                                      MoveLookup<Square8x8, P> moves) {
        ChessPiece<P> king = gameState.getPieceAt(kingPos);
        // Generate moves in all directions
        for (Square8x8 direction : KING_DIRECTIONS) {
            Square8x8 to = kingPos.offset(direction);
            // Check if the target position is on the board
            if (gameState.getBounds().contains(to)) {
                ChessPiece<P> targetPiece = gameState.getPieceAt(to);
                // Check if the target position is empty or contains an enemy piece
                if (targetPiece == null) moves.add(Move.of(king, kingPos, to));
                else moves.add(Move.of(king, kingPos, to, new CaptureComponent<>(targetPiece)));
            }
        }
    }
}
