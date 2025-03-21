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

    private static void generateKnightMoves(ChessBoard<StandardPieceType> board,
                                            ChessPiece knight,
                                            MoveCollection moves) {
        var from = board.getPiecePosition(knight);

        for (var offset : KNIGHT_MOVES) {
            var to = from.offset(offset.x(), offset.y());

            // Ensure the move stays within the board boundaries
            if (board.getBounds().contains(to)) {
                var targetPiece = board.getPieceAt(to);
                // Allow the move if the destination is empty or occupied by an opponent's piece
                if (targetPiece == null) moves.add(Move.of(knight, from, to));
                else if (targetPiece.isWhite() != knight.isWhite())
                    moves.add(Move.of(knight, from, to, new CaptureComponent(targetPiece)));
            }
        }
    }

    @Override
    public MoveCollection generateMoves(GameState<StandardPieceType> gameState) {
        var moves = new MoveCollection();
        var board = gameState.chessBoard();

        // Retrieve all knights belonging to the current player
        var knights = board.getPiecesOfTypeAndColour(StandardPieceType.KNIGHT, gameState.isWhiteTurn());

        for (var knight : knights) generateKnightMoves(board, knight, moves);

        return moves;
    }
}
