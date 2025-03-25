package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.MoveGenerator;

import java.util.List;
import java.util.Optional;

public class StaleMateRule implements GameConclusionRule<StandardPieceType> {
    private final MoveGenerator<StandardPieceType> moveGenerator;

    public StaleMateRule(MoveGenerator<StandardPieceType> moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    @Override
    public Optional<GameConclusion> detectGameEndCause(GameState<StandardPieceType> gameState) {
        MoveCollection<StandardPieceType> legalMoves = moveGenerator.generateMoves(gameState);
        if (legalMoves.isEmpty() && allKingsSafe(gameState)) {
            return Optional.of(
                    new GameConclusion(GameConclusion.Winner.NONE, "Stalemate")
            );
        }
        return Optional.empty();
    }

    private boolean allKingsSafe(GameState<StandardPieceType> gameState) {
        ChessBoard<StandardPieceType> board = gameState.getChessBoard();
        boolean isWhiteTurn = gameState.isWhiteTurn();
        List<Square> kingSquares = board
                .getSquaresWithTypeAndColour(StandardPieceType.KING, isWhiteTurn);
        if (kingSquares.isEmpty()) return true;

        // Check if the opponent can move to any of the king's squares
        gameState.switchTurn();
        MoveCollection<StandardPieceType> opponentMoves = moveGenerator.generateMoves(gameState);
        gameState.switchTurn();

        for (Square square : kingSquares) {
            if (opponentMoves.containsMoveTo(square)) return false;
        }
        return true;
    }
}
