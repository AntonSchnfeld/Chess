package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.StandardPieceType;

import java.util.Optional;

public class InsufficientMaterialRule implements GameConclusionRule<StandardPieceType> {

    @Override
    public Optional<GameConclusion> detectGameEndCause(GameState<StandardPieceType> gameState) {
        ChessBoard<StandardPieceType> board = gameState.getChessBoard();

        // Check for sufficient material according to FIDE rules
        if (hasSufficientMaterialForWin(board)) {
            return Optional.empty(); // The game can continue
        }

        // If insufficient material, return the conclusion of a draw
        return Optional.of(
                new GameConclusion(GameConclusion.Winner.NONE, "Insufficient material")
        );
    }

    private boolean hasSufficientMaterialForWin(ChessBoard<StandardPieceType> board) {
        // For no pawns on one side, the opponent must have at least +4 pawn equivalent material
        if (board.getSquaresWithType(StandardPieceType.PAWN).isEmpty()) {
            // Check if the opponent has at least enough material to overcome the opponent's king
            return hasMoreThan4PawnEquivalentMaterial(board);
        }

        return true; // If pawns are present, no need for further checks
    }

    private boolean hasMoreThan4PawnEquivalentMaterial(ChessBoard<StandardPieceType> board) {
        // Material values that roughly correspond to the FIDE system
        int materialValue = calculateMaterialValue(board);

        // The opponent should have at least +4 pawn equivalents
        return materialValue >= 4;
    }

    private int calculateMaterialValue(ChessBoard<StandardPieceType> board) {
        int value = 0;

        // Material value according to standard piece values:
        value += board.getSquaresWithType(StandardPieceType.KNIGHT).size() * 3; // Knights = 3 pawns
        value += board.getSquaresWithType(StandardPieceType.BISHOP).size() * 3; // Bishops = 3 pawns
        value += board.getSquaresWithType(StandardPieceType.ROOK).size() * 5; // Rooks = 5 pawns
        value += board.getSquaresWithType(StandardPieceType.QUEEN).size() * 9; // Queens = 9 pawns

        return value;
    }
}
