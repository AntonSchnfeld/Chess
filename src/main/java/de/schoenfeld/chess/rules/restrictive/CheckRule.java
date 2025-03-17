package de.schoenfeld.chess.rules.restrictive;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.MoveGenerator;

import java.util.Iterator;
import java.util.List;

public class CheckRule implements RestrictiveMoveRule {
    private final MoveGenerator moveGenerator;

    public CheckRule(MoveGenerator moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    @Override
    public void filterMoves(MoveCollection moves, GameState gameState) {
        // Loop through all moves
        Iterator<Move> iterator = moves.iterator();
        if (!iterator.hasNext()) return;
        for (Move move = iterator.next(); iterator.hasNext(); move = iterator.next()) {
            // Simulate move
            GameState future = move.executeOn(gameState);
            // Get valid moves in simulated future state
            MoveCollection futureMoves = moveGenerator.generateMoves(future);

            List<ChessPiece> kings = future.chessBoard()
                    .getPiecesOfType(PieceType.KING, gameState.isWhiteTurn());

            // Check if the move results in a king being in check
            for (ChessPiece king : kings) {
                if (futureMoves.containsMoveTo(future.chessBoard().getPiecePosition(king))) {
                    iterator.remove();
                }
            }
        }
    }
}
