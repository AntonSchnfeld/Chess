package de.schoenfeld.chesskit.rules.gameend;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.events.GameConclusion;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.StandardPieceType;

public class InsufficientMaterialDetector<T extends Tile> implements GameConclusionDetector<T, StandardPieceType> {
    private static final InsufficientMaterialDetector<Square8x8> STANDARD = new InsufficientMaterialDetector<>();

    public static InsufficientMaterialDetector<Square8x8> standard() {
        return STANDARD;
    }

    @Override
    public GameConclusion detectConclusion(GameState<T, StandardPieceType> gameState) {
        ChessBoard<T, StandardPieceType> board = gameState.getChessBoard();

        // Check for sufficient material according to FIDE rules
        if (hasSufficientMaterialForWin(board)) {
            return null; // The game can continue
        }

        // If insufficient material, return the conclusion of a draw
        return new GameConclusion(GameConclusion.Winner.NONE, "Insufficient material");
    }

    private boolean hasSufficientMaterialForWin(ChessBoard<T, StandardPieceType> board) {
        // For no pawns on one side, the opponent must have at least +4 pawn equivalent material
        if (board.getTilesWithType(StandardPieceType.PAWN).isEmpty()) {
            // Check if the opponent has at least enough material to overcome the opponent's king
            return hasMoreThan4PawnEquivalentMaterial(board);
        }

        return true; // If pawns are present, no need for further checks
    }

    private boolean hasMoreThan4PawnEquivalentMaterial(ChessBoard<T, StandardPieceType> board) {
        // Material values that roughly correspond to the FIDE system
        int materialValue = calculateMaterialValue(board);

        // The opponent should have at least +4 pawn equivalents
        return materialValue >= 4;
    }

    private int calculateMaterialValue(ChessBoard<T, StandardPieceType> board) {
        int value = 0;

        // Material value according to standard piece values:
        value += board.getTilesWithType(StandardPieceType.KNIGHT).size() * 3; // Knights = 3 pawns
        value += board.getTilesWithType(StandardPieceType.BISHOP).size() * 3; // Bishops = 3 pawns
        value += board.getTilesWithType(StandardPieceType.ROOK).size() * 5; // Rooks = 5 pawns
        value += board.getTilesWithType(StandardPieceType.QUEEN).size() * 9; // Queens = 9 pawns

        return value;
    }
}
