package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CastlingComponent;

import java.util.Optional;

public class CastlingRule implements GenerativeMoveRule<StandardPieceType> {

    @Override
    public MoveCollection<StandardPieceType> generateMoves(GameState<StandardPieceType> gameState) {
        MoveCollection<StandardPieceType> moves = new MoveCollection<>();
        ChessBoard<StandardPieceType> board = gameState.chessBoard(); // ImmutableChessBoard
        boolean isWhite = gameState.isWhiteTurn();

        // Get king position
        Optional<ChessPiece<StandardPieceType>> king = board
                .getPiecesOfTypeAndColour(StandardPieceType.KING, isWhite)
                .stream()
                .findFirst();
        if (king.isEmpty()) return moves; // No king found (should never happen in normal chess)

        Square kingPos = board.getPiecePosition(king.get());

        // Standard chess castling positions
        Square kingSideRookPos = isWhite ? Square.of(7, 0) : Square.of(7, 7);
        Square queenSideRookPos = isWhite ? Square.of(0, 0) : Square.of(0, 7);
        Square kingSideCastlingTarget = isWhite ? Square.of(6, 0) : Square.of(6, 7);
        Square queenSideCastlingTarget = isWhite ? Square.of(2, 0) : Square.of(2, 7);
        Square kingSideRookTarget = isWhite ? Square.of(5, 0) : Square.of(5, 7);
        Square queenSideRookTarget = isWhite ? Square.of(3, 0) : Square.of(3, 7);

        // Try adding castling moves
        checkAndAddCastlingMove(gameState, king.get(), kingPos, kingSideRookPos, kingSideCastlingTarget, kingSideRookTarget, moves);
        checkAndAddCastlingMove(gameState, king.get(), kingPos, queenSideRookPos, queenSideCastlingTarget, queenSideRookTarget, moves);

        return moves;
    }

    private void checkAndAddCastlingMove(GameState<StandardPieceType> gameState,
                                         ChessPiece<StandardPieceType> king,
                                         Square kingPos,
                                         Square rookPos,
                                         Square kingTarget,
                                         Square rookTarget,
                                         MoveCollection<StandardPieceType> moves) {
        var board = gameState.chessBoard(); // ImmutableChessBoard
        ChessPiece<StandardPieceType> rook = board.getPieceAt(rookPos);

        if (rook == null || rook.pieceType() != StandardPieceType.ROOK) return;
        if (king.hasMoved() || rook.hasMoved()) return; // Castling not allowed if either piece has moved

        // Ensure the squares between king and rook are empty
        if (!areIntermediateSquaresEmpty(board, kingPos, rookPos)) return;

        // Create the castling move
        moves.add(Move.of(king, kingPos, kingTarget, new CastlingComponent(rook, rookPos, rookTarget)));
    }

    private boolean areIntermediateSquaresEmpty(ChessBoard<StandardPieceType> board,
                                                Square from, Square to) {
        int dx = Integer.signum(to.x() - from.x()); // +1 or -1 (direction)
        for (int x = from.x() + dx; x != to.x(); x += dx) {
            if (board.getPieceAt(Square.of(x, from.y())) != null) {
                return false; // Square is occupied
            }
        }
        return true;
    }
}
