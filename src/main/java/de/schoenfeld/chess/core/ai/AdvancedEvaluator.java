package de.schoenfeld.chess.core.ai;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.*;
import de.schoenfeld.chess.rules.Rules;

import javax.swing.text.*;
import java.util.List;
import java.util.Set;

public class AdvancedEvaluator implements GameStateEvaluator {
    private static final int CHECKMATE_SCORE = 1_000_000; // Large value for checkmate
    private static final int MOBILITY_WEIGHT = 10;
    private static final int KING_SAFETY_WEIGHT = 50;
    private static final int PAWN_STRUCTURE_WEIGHT = 20;

    private final Rules rules;

    public AdvancedEvaluator(Rules rules) {
        this.rules = rules;
    }

    @Override
    public int evaluate(GameState gameState) {
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

    /** Evaluates material balance: total piece values of White - total piece values of Black */
    private int evaluateMaterial(GameState gameState) {
        int whiteValue = gameState.chessBoard().getPiecesOfColour(true)
                .stream()
                .mapToInt(p -> p.pieceType().value())
                .sum();

        int blackValue = gameState.chessBoard().getPiecesOfColour(false)
                .stream()
                .mapToInt(p -> p.pieceType().value())
                .sum();

        return whiteValue - blackValue;
    }

    /** Evaluates king safety by considering king exposure and castling */
    private int evaluateKingSafety(GameState gameState) {
        ChessBoard board = gameState.chessBoard();
        Square whiteKingPos = findKingPosition(board, true);
        Square blackKingPos = findKingPosition(board, false);

        int whiteSafety = assessKingSafety(board, whiteKingPos, true);
        int blackSafety = assessKingSafety(board, blackKingPos, false);

        return blackSafety - whiteSafety; // Higher values mean White is safer
    }

    private Square findKingPosition(ChessBoard board, boolean isWhite) {
        return board.getPiecesOfTypeAndColour(PieceType.KING, isWhite)
                .stream()
                .map(board::getPiecePosition)
                .findFirst()
                .orElseThrow();
    }

    private int assessKingSafety(ChessBoard board, Square kingPos, boolean isWhite) {
        int safetyScore = 0;
        int surroundingPieces = countSurroundingPieces(board, kingPos, isWhite);
        safetyScore += surroundingPieces * 5;

        if (kingPos.x() == 2 || kingPos.x() == 6) { // Castled kings
            safetyScore += 15;
        }
        return safetyScore;
    }

    private int countSurroundingPieces(ChessBoard board, Square pos, boolean isWhite) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Square adjacent = Square.of(pos.x() + dx, pos.y() + dy);
                ChessPiece adjacentPiece = board.getPieceAt(adjacent);
                if (board.getBounds().contains(adjacent) &&
                    adjacentPiece != null && adjacentPiece.isWhite() == isWhite) {
                    count++;
                }
            }
        }
        return count;
    }

    /** Evaluates mobility: more legal moves = better position */
    private int evaluateMobility(GameState gameState) {
        return rules.generateMoves(gameState).size();
    }

    /** Penalizes opponent's mobility: restricting opponent's moves is beneficial */
    private int evaluateEnemyMobility(GameState gameState) {
        GameState enemyState = gameState.withTurnSwitched();
        return rules.generateMoves(gameState).size();
    }

    /** Evaluates pawn structure, penalizing weaknesses like isolated or doubled pawns */
    private int evaluatePawnStructure(GameState gameState) {
        ChessBoard board = gameState.chessBoard();
        int whitePenalty = assessPawnWeaknesses(board, true);
        int blackPenalty = assessPawnWeaknesses(board, false);

        return blackPenalty - whitePenalty;
    }

    private int assessPawnWeaknesses(ChessBoard board, boolean isWhite) {
        List<ChessPiece> pawns = board.getPiecesOfTypeAndColour(PieceType.PAWN, isWhite);
        int penalty = 0;

        boolean[] hasPawnOnFile = new boolean[8]; // Tracks pawns on each file
        for (ChessPiece pawn : pawns) {
            Square pos = board.getPiecePosition(pawn);
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

    /** Handles checkmate and draw cases */
    private int evaluateGameConclusion(GameState gameState) {
        GameConclusion conclusion = rules.detectGameEndCause(gameState).orElseThrow();
        if (conclusion.isDraw()) {
            return 0; // Draw has neutral value
        }
        return conclusion.winner() == GameConclusion.Winner.WHITE ?
                CHECKMATE_SCORE : -CHECKMATE_SCORE;
    }
}
