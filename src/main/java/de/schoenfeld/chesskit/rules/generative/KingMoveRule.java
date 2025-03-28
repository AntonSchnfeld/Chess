package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.model.*;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveCollection;
import de.schoenfeld.chesskit.move.components.CaptureComponent;

import java.util.List;

public class KingMoveRule<T extends PieceType> extends AbstractGenerativeMoveRule<T> {
    private static final KingMoveRule<StandardPieceType> STANDARD =
            new KingMoveRule<>(StandardPieceType.KING);

    public KingMoveRule(T kingType) {
        super(kingType);
    }

    private static final List<Square> KING_DIRECTIONS = List.of(
            new Square(1, 0), new Square(-1, 0), new Square(0, 1), new Square(0, -1),
            new Square(1, 1), new Square(-1, -1), new Square(1, -1), new Square(-1, 1)
    );

    public static KingMoveRule<StandardPieceType> standard() {
        return STANDARD;
    }

    @Override
    protected void generatePieceMoves(GameState<T> gameState,
                                      Square kingPos,
                                      MoveCollection<T> moves) {
        ChessPiece<T> king = gameState.getPieceAt(kingPos);
        // Generate moves in all directions
        for (Square direction : KING_DIRECTIONS) {
            Square to = kingPos.offset(direction);
            // Check if the target position is on the board
            if (gameState.getBounds().contains(to)) {
                ChessPiece<T> targetPiece = gameState.getPieceAt(to);
                // Check if the target position is empty or contains an enemy piece
                if (targetPiece == null) moves.add(Move.of(king, kingPos, to));
                else moves.add(Move.of(king, kingPos, to, new CaptureComponent<>(targetPiece)));
            }
        }
    }
}
