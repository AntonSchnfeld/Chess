package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.model.*;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.CaptureComponent;

import java.util.List;

public class KnightMoveRule<T extends PieceType> extends AbstractGenerativeMoveRule<T> {
    private static final List<Square> KNIGHT_MOVES = List.of(
            new Square(2, 1), new Square(2, -1), new Square(-2, 1), new Square(-2, -1),
            new Square(1, 2), new Square(1, -2), new Square(-1, 2), new Square(-1, -2)
    );

    private static final KnightMoveRule<StandardPieceType> STANDARD =
            new KnightMoveRule<>(StandardPieceType.KNIGHT);

    public KnightMoveRule(T knightType) {
        super(knightType);
    }

    public static KnightMoveRule<StandardPieceType> standard() {
        return STANDARD;
    }

    @Override
    protected void generatePieceMoves(GameState<T> board,
                                      Square knightPos,
                                      MoveLookup<T> moves) {
        ChessPiece<T> knight = board.getPieceAt(knightPos);
        for (Square offset : KNIGHT_MOVES) {
            Square to = knightPos.offset(offset.x(), offset.y());
            // Ensure the move stays within the board boundaries
            if (board.getBounds().contains(to)) {
                ChessPiece<T> targetPiece = board.getPieceAt(to);
                // Allow the move if the destination is empty or occupied by an opponent's piece
                if (targetPiece == null) moves.add(Move.claim(knight, knightPos, to));
                else moves.add(Move.claim(knight, knightPos, to, new CaptureComponent<>(targetPiece)));
            }
        }
    }
}
