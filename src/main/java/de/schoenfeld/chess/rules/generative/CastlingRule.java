package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CastlingComponent;

import java.util.Optional;

public class CastlingRule implements GenerativeMoveRule {

    @Override
    public MoveCollection generateMoves(GameState gameState) {
        var moves = new MoveCollection();
        var board = gameState.chessBoard(); // ImmutableChessBoard
        boolean isWhite = gameState.isWhiteTurn();

        // Get king position
        Optional<ChessPiece> king = board.getPiecesOfType(PieceType.KING, isWhite).stream().findFirst();
        if (king.isEmpty()) return moves; // No king found (should never happen in normal chess)

        Position kingPos = board.getPiecePosition(king.get());

        // Standard chess castling positions
        Position kingSideRookPos = isWhite ? Position.of(7, 0) : Position.of(7, 7);
        Position queenSideRookPos = isWhite ? Position.of(0, 0) : Position.of(0, 7);
        Position kingSideCastlingTarget = isWhite ? Position.of(6, 0) : Position.of(6, 7);
        Position queenSideCastlingTarget = isWhite ? Position.of(2, 0) : Position.of(2, 7);
        Position kingSideRookTarget = isWhite ? Position.of(5, 0) : Position.of(5, 7);
        Position queenSideRookTarget = isWhite ? Position.of(3, 0) : Position.of(3, 7);

        // Try adding castling moves
        checkAndAddCastlingMove(gameState, king.get(), kingPos, kingSideRookPos, kingSideCastlingTarget, kingSideRookTarget, moves);
        checkAndAddCastlingMove(gameState, king.get(), kingPos, queenSideRookPos, queenSideCastlingTarget, queenSideRookTarget, moves);

        return moves;
    }

    private void checkAndAddCastlingMove(GameState gameState,
                                         ChessPiece king,
                                         Position kingPos,
                                         Position rookPos,
                                         Position kingTarget,
                                         Position rookTarget,
                                         MoveCollection moves) {
        var board = gameState.chessBoard(); // ImmutableChessBoard
        ChessPiece rook = board.getPieceAt(rookPos);

        if (rook == null || rook.pieceType() != PieceType.ROOK) return;
        if (king.hasMoved() || rook.hasMoved()) return; // Castling not allowed if either piece has moved

        // Ensure the squares between king and rook are empty
        if (!areIntermediateSquaresEmpty(board, kingPos, rookPos)) return;

        // Create the castling move
        moves.add(Move.of(king, kingPos, kingTarget, new CastlingComponent(rook, rookPos, rookTarget)));
    }

    private boolean areIntermediateSquaresEmpty(ImmutableChessBoard board, Position from, Position to) {
        int dx = Integer.signum(to.x() - from.x()); // +1 or -1 (direction)
        for (int x = from.x() + dx; x != to.x(); x += dx) {
            if (board.getPieceAt(Position.of(x, from.y())) != null) {
                return false; // Square is occupied
            }
        }
        return true;
    }
}
