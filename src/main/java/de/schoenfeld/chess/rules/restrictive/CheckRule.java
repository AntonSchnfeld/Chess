package de.schoenfeld.chess.rules.restrictive;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;
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
        Iterator<Move> iterator = moves.iterator();

        while (iterator.hasNext()) {
            Move move = iterator.next();

            // Simulate move
            GameState future = move.executeOn(gameState);
            MoveCollection opponentMoves = moveGenerator.generateMoves(future);

            // Get the current player's king in the simulated future state
            ChessBoard futureBoard = future.chessBoard();
            List<ChessPiece> kings = futureBoard.getPiecesOfTypeAndColour(PieceType.KING, gameState.isWhiteTurn());

            if (kings.isEmpty()) {
                // This should never happen in a normal game, but we guard against it
                continue;
            }

            ChessPiece king = kings.get(0); // There should be only one king per player
            Square kingSquare = futureBoard.getPiecePosition(king);

            // If any opponent move targets the king's position, the move is illegal
            if (opponentMoves.containsMoveTo(kingSquare)) {
                iterator.remove();
            }
        }
    }
}
