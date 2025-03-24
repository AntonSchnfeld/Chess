package de.schoenfeld.chess.core.ai;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.rules.Rules;

import java.util.List;

public class AdvancedEvaluator implements GameStateEvaluator<StandardPieceType> {
    private static final int CHECKMATE_SCORE = 1_000_000; // Large value for checkmate
    private static final int MOBILITY_WEIGHT = 10;
    private static final int KING_SAFETY_WEIGHT = 50;
    private static final int PAWN_STRUCTURE_WEIGHT = 20;

    private final Rules<StandardPieceType> rules;

    public AdvancedEvaluator(Rules<StandardPieceType> rules) {
        this.rules = rules;
    }

    @Override
    public int evaluate(GameState<StandardPieceType> gameState) {
        if (rules.detectGameEndCause(gameState).isPresent()) {
            return evaluateGameConclusion(gameState);
        }

        boolean isWhite = gameState.isWhiteTurn();
        int materialScore = evaluateMaterial(gameState);
        int kingSafetyScore = evaluateKingSafety(gameState);
        int mobilityScore = evaluateMobility(gameState);
        int enemyMobilityPenalty = evaluateEnemyMobility(gameState);
        int pawnStructureScore = evaluatePawnStructure(gameState);

        return materialScore
                + (mobilityScore * MOBILITY_WEIGHT)
                - (enemyMobilityPenalty * MOBILITY_WEIGHT)
                + (kingSafetyScore * KING_SAFETY_WEIGHT)
                + (pawnStructureScore * PAWN_STRUCTURE_WEIGHT);
    }

    private int evaluateMaterial(GameState<StandardPieceType> gameState) {
        int whiteValue = gameState.chessBoard().getSquaresWithColour(true)
                .stream()
                .mapToInt(p -> gameState.getPieceAt(p).pieceType().value())
                .sum();

        int blackValue = gameState.chessBoard().getSquaresWithColour(false)
                .stream()
                .mapToInt(p -> gameState.getPieceAt(p).pieceType().value())
                .sum();

        return whiteValue - blackValue;
    }

    private int evaluateKingSafety(GameState<StandardPieceType> gameState) {
        ChessBoard<StandardPieceType> board = gameState.chessBoard();
        Square whiteKingPos = findKingPosition(board, true);
        Square blackKingPos = findKingPosition(board, false);

        int whiteSafety = assessKingSafety(board, whiteKingPos, true);
        int blackSafety = assessKingSafety(board, blackKingPos, false);

        return blackSafety - whiteSafety; // Higher values mean White is safer
    }

    private Square findKingPosition(ChessBoard<StandardPieceType> board, boolean isWhite) {
        return board.getSquaresWithTypeAndColour(StandardPieceType.KING, isWhite)
                .stream()
                .findFirst()
                .orElseThrow();
    }

    private int assessKingSafety(ChessBoard<StandardPieceType> board,
                                 Square kingPos, boolean isWhite) {
        int safetyScore = 0;
        int surroundingPieces = countSurroundingPieces(board, kingPos, isWhite);
        safetyScore += surroundingPieces * 5;

        if (kingPos.x() == 2 || kingPos.x() == 6) { // Castled kings
            safetyScore += 15;
        }
        return safetyScore;
    }

    private int countSurroundingPieces(ChessBoard<StandardPieceType> board,
                                       Square pos, boolean isWhite) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Square adjacent = Square.of(pos.x() + dx, pos.y() + dy);
                ChessPiece<StandardPieceType> adjacentPiece = board.getPieceAt(adjacent);
                if (board.getBounds().contains(adjacent) &&
                        adjacentPiece != null && adjacentPiece.isWhite() == isWhite) {
                    count++;
                }
            }
        }
        return count;
    }

    private int evaluateMobility(GameState<StandardPieceType> gameState) {
        return rules.generateMoves(gameState).size();
    }

    private int evaluateEnemyMobility(GameState<StandardPieceType> gameState) {
        GameState<StandardPieceType> enemyState = gameState.withTurnSwitched();
        return rules.generateMoves(gameState).size();
    }

    private int evaluatePawnStructure(GameState<StandardPieceType> gameState) {
        ChessBoard<StandardPieceType> board = gameState.chessBoard();
        int whitePenalty = assessPawnWeaknesses(board, true);
        int blackPenalty = assessPawnWeaknesses(board, false);

        return blackPenalty - whitePenalty;
    }

    private int assessPawnWeaknesses(ChessBoard<StandardPieceType> board, boolean isWhite) {
        List<Square> pawnSquares = board.getSquaresWithTypeAndColour(StandardPieceType.PAWN, isWhite);
        int penalty = 0;

        boolean[] hasPawnOnFile = new boolean[8]; // Tracks pawns on each file
        for (Square pos : pawnSquares) {
            int file = pos.x();
            if (hasPawnOnFile[file]) {
                penalty += 10; // Doubled pawn penalty
            }
            hasPawnOnFile[file] = true;

            boolean isolated = (file == 0 || !hasPawnOnFile[file - 1]) &&
                    (file == 7 || !hasPawnOnFile[file + 1]);
            if (isolated) {
                penalty += 15; // Isolated pawn penalty
            }
        }
        return penalty;
    }

    private int evaluateGameConclusion(GameState<StandardPieceType> gameState) {
        GameConclusion conclusion = rules.detectGameEndCause(gameState).orElseThrow();
        if (conclusion.isDraw()) {
            return 0; // Draw has neutral value
        }
        return conclusion.winner() == GameConclusion.Winner.WHITE ?
                CHECKMATE_SCORE : -CHECKMATE_SCORE;
    }
}
