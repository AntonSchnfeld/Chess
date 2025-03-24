package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CastlingComponent;

import java.util.List;

public class CastlingRule implements GenerativeMoveRule<StandardPieceType> {

    @Override
    public MoveCollection<StandardPieceType> generateMoves(GameState<StandardPieceType> gameState) {
        MoveCollection<StandardPieceType> moves = new MoveCollection<>();
        ChessBoard<StandardPieceType> board = gameState.chessBoard(); // Immutable board
        boolean isWhite = gameState.isWhiteTurn();

        // Get all kings of the current player
        List<Square> kings = board.getSquaresWithType(StandardPieceType.KING)
                .stream()
                .filter(square -> board.getPieceAt(square).isWhite() == isWhite)
                .toList();

        for (Square kingPos : kings) {
            // Standard chess castling positions
            Square kingSideRookPos = isWhite ? Square.of(7, 0) : Square.of(7, 7);
            Square queenSideRookPos = isWhite ? Square.of(0, 0) : Square.of(0, 7);
            Square kingSideCastlingTarget = isWhite ? Square.of(6, 0) : Square.of(6, 7);
            Square queenSideCastlingTarget = isWhite ? Square.of(2, 0) : Square.of(2, 7);
            Square kingSideRookTarget = isWhite ? Square.of(5, 0) : Square.of(5, 7);
            Square queenSideRookTarget = isWhite ? Square.of(3, 0) : Square.of(3, 7);

            // Try adding castling moves
            checkAndAddCastlingMove(gameState, kingPos, kingSideRookPos, kingSideCastlingTarget, kingSideRookTarget, moves);
            checkAndAddCastlingMove(gameState, kingPos, queenSideRookPos, queenSideCastlingTarget, queenSideRookTarget, moves);
        }

        return moves;
    }

    private void checkAndAddCastlingMove(GameState<StandardPieceType> gameState,
                                         Square kingPos,
                                         Square rookPos,
                                         Square kingTarget,
                                         Square rookTarget,
                                         MoveCollection<StandardPieceType> moves) {

        if (!gameState.isOccupied(rookPos) ||
                gameState.getPieceAt(rookPos).pieceType() != StandardPieceType.ROOK) return;
        if (gameState.getPieceAt(kingPos).hasMoved() ||
                gameState.getPieceAt(rookPos).hasMoved()) return;

        // Ensure the squares between king and rook are empty
        if (!areIntermediateSquaresEmpty(gameState, kingPos, rookPos)) return;

        // Create the castling move
        moves.add(Move.of(gameState.getPieceAt(kingPos), kingPos, kingTarget,
                new CastlingComponent(gameState.getPieceAt(rookPos), rookPos, rookTarget)));
    }

    private boolean areIntermediateSquaresEmpty(ChessBoard<StandardPieceType> board, Square from, Square to) {
        int dx = Integer.signum(to.x() - from.x()); // +1 or -1 for direction
        for (int x = from.x() + dx; x != to.x(); x += dx) {
            if (board.isOccupied(Square.of(x, from.y()))) {
                return false; // Square is occupied
            }
        }
        return true;
    }
}
