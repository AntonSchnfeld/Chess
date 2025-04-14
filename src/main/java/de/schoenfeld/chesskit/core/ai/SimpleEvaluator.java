package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.Rules;

import java.util.List;

public class SimpleEvaluator implements GameStateEvaluator<Square8x8, StandardPieceType> {
    private final Rules<Square8x8, StandardPieceType> rules;

    public SimpleEvaluator(Rules<Square8x8, StandardPieceType> rules) {
        this.rules = rules;
    }

    @Override
    public int evaluate(GameState<Square8x8, StandardPieceType> gameState) {
        int evaluation = 0;

        MoveLookup<Square8x8, StandardPieceType> validMoves = rules.generateMoves(gameState);
        gameState.switchTurn();
        MoveLookup<Square8x8, StandardPieceType> enemyMoves = rules.generateMoves(gameState);
        gameState.switchTurn();

        evaluation += validMoves.size() * 5;
        evaluation -= enemyMoves.size() * 5;

        for (Square8x8 square8x8 : gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor())) {
            if (enemyMoves.containsMoveTo(square8x8))
                evaluation -= 500;
        }
        for (Square8x8 square8x8 : gameState.getTilesWithTypeAndColour(StandardPieceType.KING, gameState.getColor().opposite())) {
            if (enemyMoves.containsMoveTo(square8x8))
                evaluation += 500;
        }
        for (Square8x8 square8x8 : gameState.getOccupiedTiles()) {
            ChessPiece<StandardPieceType> piece = gameState.getPieceAt(square8x8);
            if (piece == null) continue;

            int factor = (piece.color() == gameState.getColor()) ? 1 : -1;
            evaluation += piece.pieceType().value() * factor;
            evaluation += getCentralityBonus(square8x8) * factor;
            evaluation += developmentBonus(piece, square8x8, (factor > 0) ? Color.WHITE : Color.BLACK);
        }

        evaluation -= evaluateSimplePawnStructure(gameState, gameState.getColor());

        return evaluation;
    }

    int developmentBonus(ChessPiece<StandardPieceType> piece, Square8x8 square8x8, Color isFriendly) {
        if (piece.pieceType() == StandardPieceType.KNIGHT || piece.pieceType() == StandardPieceType.BISHOP) {
            if (isFriendly == Color.WHITE
                    && ((piece.color() == Color.WHITE && square8x8.y() != 0)
                    || (piece.color() == Color.BLACK && square8x8.y() != 7))) {
                return 20;
            } else if (isFriendly == Color.BLACK
                    && ((piece.color() == Color.WHITE && square8x8.y() != 0)
                    || (piece.color() == Color.BLACK && square8x8.y() != 7))) {
                return -20;
            }
        }
        return 0;
    }

    int getCentralityBonus(Square8x8 square8x8) {
        int x = square8x8.x(), y = square8x8.y();
        int centerDistanceX = Math.abs(3 - x) + Math.abs(4 - x);
        int centerDistanceY = Math.abs(3 - y) + Math.abs(4 - y);
        return 20 - (centerDistanceX + centerDistanceY);
    }

    int evaluateSimplePawnStructure(GameState<Square8x8, StandardPieceType> state, Color color) {
        int penalty = 0;
        List<Square8x8> pawnSquare8x8s = state.getTilesWithTypeAndColour(StandardPieceType.PAWN, color);
        for (Square8x8 pawn : pawnSquare8x8s) {
            boolean leftFilePawn = pawnSquare8x8s.contains(Square8x8.of(pawn.x() - 1, pawn.y()));
            boolean rightFilePawn = pawnSquare8x8s.contains(Square8x8.of(pawn.x() + 1, pawn.y()));
            if (!leftFilePawn && !rightFilePawn) penalty += 15;
        }
        return penalty;
    }
}
