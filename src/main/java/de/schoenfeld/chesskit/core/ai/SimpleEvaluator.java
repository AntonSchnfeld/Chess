package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.Square;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.Rules;

import java.util.List;

public class SimpleEvaluator implements GameStateEvaluator<StandardPieceType> {
    private final Rules<StandardPieceType> rules;

    public SimpleEvaluator(Rules<StandardPieceType> rules) {
        this.rules = rules;
    }

    @Override
    public int evaluate(GameState<StandardPieceType> gameState) {
        int evaluation = 0;

        MoveLookup<StandardPieceType> validMoves = rules.generateMoves(gameState);
        gameState.switchTurn();
        MoveLookup<StandardPieceType> enemyMoves = rules.generateMoves(gameState);
        gameState.switchTurn();

        // Mobilität
        evaluation += validMoves.size() * 5;
        evaluation -= enemyMoves.size() * 5;

        // Königsicherheit und Schachmatt
        for (Square square : gameState.getSquaresWithTypeAndColour(StandardPieceType.KING, gameState.isWhiteTurn())) {
            if (enemyMoves.containsMoveTo(square))
                evaluation -= 500;
        }
        for (Square square : gameState.getSquaresWithTypeAndColour(StandardPieceType.KING, !gameState.isWhiteTurn())) {
            if (enemyMoves.containsMoveTo(square))
                evaluation += 500;
        }
        // Material und Positionierung der Figuren
        for (Square square : gameState.getOccupiedSquares()) {
            ChessPiece<StandardPieceType> piece = gameState.getPieceAt(square);
            if (piece == null) continue;

            int factor = piece.isWhite() == gameState.isWhiteTurn() ? 1 : -1;
            evaluation += piece.pieceType().value() * factor;
            evaluation += getCentralityBonus(square) * factor;
            evaluation += developmentBonus(piece, square, factor > 0);
        }

        // Bauernstruktur-Strafe
        evaluation -= evaluateSimplePawnStructure(gameState, gameState.isWhiteTurn());

        return evaluation;
    }

    int developmentBonus(ChessPiece<StandardPieceType> piece, Square square, boolean isFriendly) {
        if (piece.pieceType() == StandardPieceType.KNIGHT || piece.pieceType() == StandardPieceType.BISHOP) {
            if (isFriendly && ((piece.isWhite() && square.y() != 0) || (!piece.isWhite() && square.y() != 7))) {
                return 20;
            } else if (!isFriendly && ((piece.isWhite() && square.y() != 0) || (!piece.isWhite() && square.y() != 7))) {
                return -20;
            }
        }
        return 0;
    }

    int getCentralityBonus(Square square) {
        int x = square.x(), y = square.y();
        int centerDistanceX = Math.abs(3 - x) + Math.abs(4 - x);
        int centerDistanceY = Math.abs(3 - y) + Math.abs(4 - y);
        return 20 - (centerDistanceX + centerDistanceY);
    }

    int evaluateSimplePawnStructure(GameState<StandardPieceType> state, boolean isWhite) {
        int penalty = 0;
        List<Square> pawnSquares = state.getSquaresWithTypeAndColour(StandardPieceType.PAWN, isWhite);
        for (Square pawn : pawnSquares) {
            boolean leftFilePawn   = pawnSquares.contains(Square.of(pawn.x() - 1, pawn.y()));
            boolean rightFilePawn  = pawnSquares.contains(Square.of(pawn.x() + 1, pawn.y()));
            if (!leftFilePawn && !rightFilePawn) penalty += 15; // isolierter Bauer
        }
        return penalty;
    }
}
