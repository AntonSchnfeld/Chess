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

public class KingMoveRule implements GenerativeMoveRule<StandardPieceType> {
    private static final List<Square> KING_DIRECTIONS = List.of(
            new Square(1, 0), new Square(-1, 0), new Square(0, 1), new Square(0, -1),
            new Square(1, 1), new Square(-1, -1), new Square(1, -1), new Square(-1, 1)
    );

    private static void generateKingMoves(GameState<StandardPieceType> gameState,
                                          Square kingPos,
                                          MoveCollection<StandardPieceType> moves) {
        ChessPiece<StandardPieceType> king = gameState.getPieceAt(kingPos);
        // Generate moves in all directions
        for (Square direction : KING_DIRECTIONS) {
            Square to = kingPos.offset(direction);
            // Check if the target position is on the board
            if (gameState.getBounds().contains(to)) {
                ChessPiece<StandardPieceType> targetPiece = gameState.getPieceAt(to);
                // Check if the target position is empty or contains an enemy piece
                if (targetPiece == null) moves.add(Move.of(king, kingPos, to));
                // Capture
                else if (targetPiece.isWhite() != king.isWhite())
                    moves.add(Move.of(king, kingPos, to, new CaptureComponent<>(targetPiece)));
            }
        }
    }

    @Override
    public MoveCollection<StandardPieceType> generateMoves(GameState<StandardPieceType> gameState) {
        MoveCollection<StandardPieceType> moves = new MoveCollection<>();

        List<Square> kings = gameState
                .getSquaresWithTypeAndColour(StandardPieceType.KING, gameState.isWhiteTurn())
                .stream()
                .toList();

        for (Square king : kings) generateKingMoves(gameState, king, moves);

        return moves;
    }
}
