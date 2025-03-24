package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;

import java.util.List;

public class KnightMoveRule implements GenerativeMoveRule<StandardPieceType> {
    private static final List<Square> KNIGHT_MOVES = List.of(
            new Square(2, 1), new Square(2, -1), new Square(-2, 1), new Square(-2, -1),
            new Square(1, 2), new Square(1, -2), new Square(-1, 2), new Square(-1, -2)
    );

    private static void generateKnightMoves(GameState<StandardPieceType> board,
                                            Square knightPos,
                                            MoveCollection<StandardPieceType> moves) {
        ChessPiece<StandardPieceType> knight = board.getPieceAt(knightPos);
        for (Square offset : KNIGHT_MOVES) {
            Square to = knightPos.offset(offset.x(), offset.y());
            // Ensure the move stays within the board boundaries
            if (board.getBounds().contains(to)) {
                ChessPiece<StandardPieceType> targetPiece = board.getPieceAt(to);
                // Allow the move if the destination is empty or occupied by an opponent's piece
                if (targetPiece == null) moves.add(Move.of(knight, knightPos, to));
                else moves.add(Move.of(knight, knightPos, to, new CaptureComponent<>(targetPiece)));
            }
        }
    }

    @Override
    public MoveCollection<StandardPieceType> generateMoves(GameState<StandardPieceType> gameState) {
        MoveCollection<StandardPieceType> moves = new MoveCollection<>();

        // Retrieve all knights belonging to the current player
        List<Square> knightSquares = gameState
                .getSquaresWithTypeAndColour(StandardPieceType.KNIGHT, gameState.isWhiteTurn());

        for (Square square : knightSquares) generateKnightMoves(gameState, square, moves);

        return moves;
    }
}
