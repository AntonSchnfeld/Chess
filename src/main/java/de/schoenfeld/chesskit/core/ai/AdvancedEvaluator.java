package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.events.GameConclusion;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.rules.Rules;

import java.util.List;

public class AdvancedEvaluator implements GameStateEvaluator<Square8x8, StandardPieceType> {
    private static final int CHECKMATE_SCORE = 1_000_000; // Large value for checkmate
    private static final int MOBILITY_WEIGHT = 10;
    private static final int KING_SAFETY_WEIGHT = 50;
    private static final int PAWN_STRUCTURE_WEIGHT = 20;

    private final Rules<Square8x8, StandardPieceType> rules;

    public AdvancedEvaluator(Rules<Square8x8, StandardPieceType> rules) {
        this.rules = rules;
    }

    @Override
    public int evaluate(GameState<Square8x8, StandardPieceType> gameState) {
        if (rules.detectConclusion(gameState) != null) {
            return evaluateGameConclusion(gameState);
        }

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

    private int evaluateMaterial(GameState<Square8x8, StandardPieceType> gameState) {
        int whiteValue = gameState.getChessBoard().getTilesWithColour(Color.WHITE)
                .stream()
                .mapToInt(p -> gameState.getPieceAt(p).pieceType().value())
                .sum();

        int blackValue = gameState.getChessBoard().getTilesWithColour(Color.BLACK)
                .stream()
                .mapToInt(p -> gameState.getPieceAt(p).pieceType().value())
                .sum();

        return whiteValue - blackValue;
    }

    private int evaluateKingSafety(GameState<Square8x8, StandardPieceType> gameState) {
        ChessBoard<Square8x8, StandardPieceType> board = gameState.getChessBoard();
        Square8x8 whiteKingPos = findKingPosition(board, Color.WHITE);
        Square8x8 blackKingPos = findKingPosition(board, Color.BLACK);

        int whiteSafety = assessKingSafety(board, whiteKingPos, Color.WHITE);
        int blackSafety = assessKingSafety(board, blackKingPos, Color.BLACK);

        return blackSafety - whiteSafety; // Higher values mean White is safer
    }

    private Square8x8 findKingPosition(ChessBoard<Square8x8, StandardPieceType> board, Color isWhite) {
        return board.getTilesWithTypeAndColour(StandardPieceType.KING, isWhite)
                .stream()
                .findFirst()
                .orElseThrow();
    }

    private int assessKingSafety(ChessBoard<Square8x8, StandardPieceType> board,
                                 Square8x8 kingPos, Color isWhite) {
        int safetyScore = 0;
        int surroundingPieces = countSurroundingPieces(board, kingPos, isWhite);
        safetyScore += surroundingPieces * 5;

        if (kingPos.x() == 2 || kingPos.x() == 6) { // Castled kings
            safetyScore += 15;
        }
        return safetyScore;
    }

    private int countSurroundingPieces(ChessBoard<Square8x8, StandardPieceType> board,
                                       Square8x8 pos, Color isWhite) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Square8x8 adjacent = Square8x8.of(pos.x() + dx, pos.y() + dy);
                ChessPiece<StandardPieceType> adjacentPiece = board.getPieceAt(adjacent);
                if (board.getBounds().contains(adjacent) &&
                        adjacentPiece != null && adjacentPiece.color() == isWhite) {
                    count++;
                }
            }
        }
        return count;
    }

    private int evaluateMobility(GameState<Square8x8, StandardPieceType> gameState) {
        return rules.generateMoves(gameState).size();
    }

    private int evaluateEnemyMobility(GameState<Square8x8, StandardPieceType> gameState) {
        gameState.switchTurn();
        int eval = rules.generateMoves(gameState).size();
        gameState.switchTurn();
        return eval;
    }

    private int evaluatePawnStructure(GameState<Square8x8, StandardPieceType> gameState) {
        ChessBoard<Square8x8, StandardPieceType> board = gameState.getChessBoard();
        int whitePenalty = assessPawnWeaknesses(board, Color.WHITE);
        int blackPenalty = assessPawnWeaknesses(board, Color.BLACK);

        return blackPenalty - whitePenalty;
    }

    private int assessPawnWeaknesses(ChessBoard<Square8x8, StandardPieceType> board, Color isWhite) {
        List<Square8x8> pawnSquare8x8s = board.getTilesWithTypeAndColour(StandardPieceType.PAWN, isWhite);
        int penalty = 0;

        boolean[] hasPawnOnFile = new boolean[8]; // Tracks pawns on each file
        for (Square8x8 pos : pawnSquare8x8s) {
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

    private int evaluateGameConclusion(GameState<Square8x8, StandardPieceType> gameState) {
        GameConclusion conclusion = rules.detectConclusion(gameState);
        if (conclusion == null) throw new IllegalStateException("Game state is not in a conclusion state");
        if (conclusion.isDraw()) {
            return 0; // Draw has neutral value
        }
        return conclusion.winner() == GameConclusion.Winner.WHITE ?
                CHECKMATE_SCORE : -CHECKMATE_SCORE;
    }
}
