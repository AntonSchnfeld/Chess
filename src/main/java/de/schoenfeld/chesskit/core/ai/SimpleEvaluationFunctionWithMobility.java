package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.Square;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.MoveCollection;
import de.schoenfeld.chesskit.rules.MoveGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleEvaluationFunctionWithMobility implements GameStateEvaluator<StandardPieceType> {

    // Bonus per legal move.
    private static final int MOBILITY_BONUS = 5;
    // Pawn structure penalties.
    private static final int DOUBLED_PAWN_PENALTY = 10;
    private static final int ISOLATED_PAWN_PENALTY = 20;
    // Huge bonus for win/loss.
    private static final int WIN_BONUS = 100000;
    // Capture bonus factor (20% extra bonus on the material difference).
    private static final double CAPTURE_BONUS_FACTOR = 0.20;
    private final MoveGenerator<StandardPieceType> moveGenerator;

    public SimpleEvaluationFunctionWithMobility(MoveGenerator<StandardPieceType> moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    private static int getPenalty(Map<Integer, Integer> fileCounts, List<Square> pawnSquares) {
        int penalty = 0;
        // Penalize doubled pawns: for each extra pawn in a file, apply a penalty.
        for (Integer count : fileCounts.values()) {
            if (count > 1) {
                penalty += (count - 1) * DOUBLED_PAWN_PENALTY;
            }
        }

        // Penalize isolated pawns: if a pawn's file has no adjacent friendly pawn.
        for (Square pawnPos : pawnSquares) {
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
    public int evaluate(GameState<StandardPieceType> gameState) {
        if (gameState == null) {
            throw new NullPointerException("GameState must not be null");
        }

        ChessBoard<StandardPieceType> board = gameState.getChessBoard();

        // Check for win conditions.
        // If the opponent's king is missing, return a huge win bonus.
        if (board.getSquaresWithTypeAndColour(StandardPieceType.KING, !gameState.isWhiteTurn()).isEmpty()) {
            return gameState.isWhiteTurn() ? WIN_BONUS : -WIN_BONUS;
        }
        // If our king is missing, return a huge loss.
        if (board.getSquaresWithTypeAndColour(StandardPieceType.KING, gameState.isWhiteTurn()).isEmpty()) {
            return gameState.isWhiteTurn() ? -WIN_BONUS : WIN_BONUS;
        }

        // Material evaluation.
        int materialScore = 0;
        for (Square square : board.getSquaresWithColour(gameState.isWhiteTurn()))
            materialScore += getPieceValue(gameState.getPieceAt(square).pieceType());
        for (Square square : board.getSquaresWithColour(!gameState.isWhiteTurn()))
            materialScore -= getPieceValue(gameState.getPieceAt(square).pieceType());

        // Add capture bonus: additional bonus equal to 20% of the material score.
        int captureBonus = (int) (materialScore * CAPTURE_BONUS_FACTOR);

        // Mobility evaluation.
        boolean isWhite = gameState.isWhiteTurn();
        gameState.setIsWhiteTurn(true);
        MoveCollection<StandardPieceType> whiteMoves = moveGenerator.generateMoves(gameState);
        gameState.setIsWhiteTurn(false);
        MoveCollection<StandardPieceType> blackMoves = moveGenerator.generateMoves(gameState);
        gameState.setIsWhiteTurn(isWhite);
        int mobilityScore = MOBILITY_BONUS * (whiteMoves.size() - blackMoves.size());

        // Pawn structure evaluation.
        int whitePawnPenalty = evaluatePawnStructure(board, true);
        int blackPawnPenalty = evaluatePawnStructure(board, false);
        int pawnScore = -whitePawnPenalty + blackPawnPenalty;

        return materialScore + captureBonus + mobilityScore + pawnScore;
    }

    private int evaluatePawnStructure(ChessBoard<StandardPieceType> board, boolean isWhite) {
        List<Square> pawnSquares = board.getSquaresWithTypeAndColour(StandardPieceType.PAWN, isWhite);
        if (pawnSquares.isEmpty()) return 0;

        // Map from file (x coordinate) to the count of pawns.
        Map<Integer, Integer> fileCounts = new HashMap<>();
        for (Square square : pawnSquares) {
            int file = square.x();
            fileCounts.put(file, fileCounts.getOrDefault(file, 0) + 1);
        }

        return getPenalty(fileCounts, pawnSquares);
    }

    private int getPieceValue(PieceType type) {
        return type.value();
    }
}