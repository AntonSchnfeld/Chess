package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.MoveGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleEvaluationFunctionWithMobility implements GameStateEvaluator<Square8x8, StandardPieceType> {

    // Bonus per legal move.
    private static final int MOBILITY_BONUS = 5;
    // Pawn structure penalties.
    private static final int DOUBLED_PAWN_PENALTY = 10;
    private static final int ISOLATED_PAWN_PENALTY = 20;
    // Huge bonus for win/loss.
    private static final int WIN_BONUS = 100000;
    // Capture bonus factor (20% extra bonus on the material difference).
    private static final double CAPTURE_BONUS_FACTOR = 0.20;
    private final MoveGenerator<Square8x8, StandardPieceType> moveGenerator;

    public SimpleEvaluationFunctionWithMobility(MoveGenerator<Square8x8, StandardPieceType> moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    private static int getPenalty(Map<Integer, Integer> fileCounts, List<Square8x8> pawnSquare8x8s) {
        int penalty = 0;
        // Penalize doubled pawns: for each extra pawn in a file, apply a penalty.
        for (Integer count : fileCounts.values()) {
            if (count > 1) {
                penalty += (count - 1) * DOUBLED_PAWN_PENALTY;
            }
        }

        // Penalize isolated pawns: if a pawn's file has no adjacent friendly pawn.
        for (Square8x8 pawnPos : pawnSquare8x8s) {
            int file = pawnPos.x();
            boolean hasLeft = fileCounts.containsKey(file - 1);
            boolean hasRight = fileCounts.containsKey(file + 1);
            if (!hasLeft && !hasRight) {
                penalty += ISOLATED_PAWN_PENALTY;
            }
        }
        return penalty;
    }

    @Override
    public int evaluate(GameState<Square8x8, StandardPieceType> gameState) {
        if (gameState == null) {
            throw new NullPointerException("GameState must not be null");
        }

        ChessBoard<Square8x8, StandardPieceType> board = gameState.getChessBoard();

        // Check for win conditions.
        // If the opponent's king is missing, return a huge win bonus.
        if (board.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor().opposite()).isEmpty()) {
            return gameState.getColor() == Color.WHITE ? WIN_BONUS : -WIN_BONUS;
        }
        // If our king is missing, return a huge loss.
        if (board.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor()).isEmpty()) {
            return gameState.getColor() == Color.WHITE ? -WIN_BONUS : WIN_BONUS;
        }

        // Material evaluation.
        int materialScore = 0;
        for (Square8x8 square8x8 : board.getTilesWithColour(gameState.getColor()))
            materialScore += getPieceValue(gameState.getPieceAt(square8x8).pieceType());
        for (Square8x8 square8x8 : board.getTilesWithColour(gameState.getColor().opposite()))
            materialScore -= getPieceValue(gameState.getPieceAt(square8x8).pieceType());

        // Add capture bonus: additional bonus equal to 20% of the material score.
        int captureBonus = (int) (materialScore * CAPTURE_BONUS_FACTOR);

        // Mobility evaluation.
        Color isWhite = gameState.getColor();
        gameState.setIsWhiteTurn(isWhite);
        MoveLookup<Square8x8, StandardPieceType> whiteMoves = moveGenerator.generateMoves(gameState);
        gameState.setIsWhiteTurn(isWhite);
        MoveLookup<Square8x8, StandardPieceType> blackMoves = moveGenerator.generateMoves(gameState);
        gameState.setIsWhiteTurn(isWhite);
        int mobilityScore = MOBILITY_BONUS * (whiteMoves.size() - blackMoves.size());

        // Pawn structure evaluation.
        int whitePawnPenalty = evaluatePawnStructure(board, Color.WHITE);
        int blackPawnPenalty = evaluatePawnStructure(board, Color.BLACK);
        int pawnScore = -whitePawnPenalty + blackPawnPenalty;

        return materialScore + captureBonus + mobilityScore + pawnScore;
    }

    private int evaluatePawnStructure(ChessBoard<Square8x8, StandardPieceType> board, Color isWhite) {
        List<Square8x8> pawnSquare8x8s = board.getTilesWithTypeAndColour(StandardPieceType.PAWN, isWhite);
        if (pawnSquare8x8s.isEmpty()) return 0;

        // Map from file (x coordinate) to the count of pawns.
        Map<Integer, Integer> fileCounts = new HashMap<>();
        for (Square8x8 square8x8 : pawnSquare8x8s) {
            int file = square8x8.x();
            fileCounts.put(file, fileCounts.getOrDefault(file, 0) + 1);
        }

        return getPenalty(fileCounts, pawnSquare8x8s);
    }

    private int getPieceValue(PieceType type) {
        return type.value();
    }
}