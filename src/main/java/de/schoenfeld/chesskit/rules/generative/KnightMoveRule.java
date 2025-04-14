package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.*;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.CaptureComponent;

import java.util.List;

public class KnightMoveRule<P extends PieceType> extends AbstractGenerativeMoveRule<Square8x8, P> {
    private static final List<Square8x8> KNIGHT_MOVES = List.of(
            Square8x8.of(2, 1), Square8x8.of(2, -1),
            Square8x8.of(-2, 1), Square8x8.of(-2, -1),
            Square8x8.of(1, 2), Square8x8.of(1, -2),
            Square8x8.of(-1, 2), Square8x8.of(-1, -2)
    );

    private static final KnightMoveRule<StandardPieceType> STANDARD =
            new KnightMoveRule<>(StandardPieceType.KNIGHT);

    public KnightMoveRule(P knightType) {
        super(knightType);
    }

    public static KnightMoveRule<StandardPieceType> standard() {
        return STANDARD;
    }

    @Override
    protected void generatePieceMoves(GameState<Square8x8, P> board,
                                      Square8x8 knightPos,
                                      MoveLookup<Square8x8, P> moves) {
        ChessPiece<P> knight = board.getPieceAt(knightPos);
        for (Square8x8 offset : KNIGHT_MOVES) {
            Square8x8 to = knightPos.offset(offset.x(), offset.y());
            // Ensure the move stays within the board boundaries
            if (board.getBounds().contains(to)) {
                ChessPiece<P> targetPiece = board.getPieceAt(to);
                // Allow the move if the destination is empty or occupied by an opponent's piece
                if (targetPiece == null) moves.add(Move.of(knight, knightPos, to));
                else moves.add(Move.of(knight, knightPos, to, new CaptureComponent<>(targetPiece)));
            }
        }
    }
}
