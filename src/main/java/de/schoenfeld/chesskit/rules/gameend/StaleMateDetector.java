package de.schoenfeld.chesskit.rules.gameend;

import de.schoenfeld.chesskit.board.ChessBoard;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.events.GameConclusion;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.MoveGenerator;
import de.schoenfeld.chesskit.rules.RestrictiveMoveGenerator;
import de.schoenfeld.chesskit.rules.generative.*;
import de.schoenfeld.chesskit.rules.generative.sliding.BishopMoveRule;
import de.schoenfeld.chesskit.rules.generative.sliding.QueenMoveRule;
import de.schoenfeld.chesskit.rules.generative.sliding.RookMoveRule;
import de.schoenfeld.chesskit.rules.restrictive.CheckRule;
import de.schoenfeld.chesskit.rules.restrictive.FriendlyFireRule;
import de.schoenfeld.chesskit.rules.restrictive.NoCastlingThroughCheckRule;

import java.util.List;

public class StaleMateDetector<T extends Tile, P extends PieceType> implements GameConclusionDetector<T, P> {
    private static final StaleMateDetector<Square8x8, StandardPieceType> STANDARD = new StaleMateDetector<>(
            new RestrictiveMoveGenerator<>(
                    List.of(
                            PawnMoveRule.standard(),
                            KnightMoveRule.standard(),
                            BishopMoveRule.standard(),
                            RookMoveRule.standard(),
                            QueenMoveRule.standard(),
                            KingMoveRule.standard(),
                            CastlingRule.standard(),
                            EnPassantRule.standard()
                    ),
                    List.of(
                            FriendlyFireRule.standard(),
                            NoCastlingThroughCheckRule.standard(),
                            CheckRule.standard()
                    )
            ),
            StandardPieceType.KING
    );

    public static StaleMateDetector<Square8x8, StandardPieceType> standard() {
        return STANDARD;
    }

    private final MoveGenerator<T, P> moveGenerator;
    private final P kingType;

    public StaleMateDetector(MoveGenerator<T, P> moveGenerator, P kingType) {
        this.moveGenerator = moveGenerator;
        this.kingType = kingType;
    }

    @Override
    public GameConclusion detectConclusion(GameState<T, P> gameState) {
        MoveLookup<T, P> legalMoves = moveGenerator.generateMoves(gameState);
        if (legalMoves.isEmpty() && allKingsSafe(gameState)) {
            return new GameConclusion(GameConclusion.Winner.NONE, "Stalemate");
        }
        return null;
    }

    private boolean allKingsSafe(GameState<T, P> gameState) {
        ChessBoard<T, P> board = gameState.getChessBoard();
        Color isWhiteTurn = gameState.getColor();
        List<T> kingSquare8x8s = board
                .getTilesWithTypeAndColour(kingType, isWhiteTurn);
        if (kingSquare8x8s.isEmpty()) return true;

        // Check if the opponent can move to any of the king's squares
        gameState.switchTurn();
        MoveLookup<T, P> opponentMoves = moveGenerator.generateMoves(gameState);
        gameState.switchTurn();

        for (T square8x8 : kingSquare8x8s) {
            if (opponentMoves.containsMoveTo(square8x8)) return false;
        }
        return true;
    }
}
