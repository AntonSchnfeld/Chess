package de.schoenfeld.chesskit.rules.gameend;

import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.events.GameConclusion;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
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

public class CheckMateDetector<T extends Tile, P extends PieceType> implements GameConclusionDetector<T, P> {
    private static final CheckMateDetector<Square8x8, StandardPieceType> STANDARD = new CheckMateDetector<>(
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

    public static CheckMateDetector<Square8x8, StandardPieceType> standard() {
        return STANDARD;
    }

    private final MoveGenerator<T, P> moveGenerator;
    private final P kingType;

    public CheckMateDetector(MoveGenerator<T, P> moveGenerator,
                             P kingType) {
        this.moveGenerator = moveGenerator;
        this.kingType = kingType;
    }

    @Override
    public GameConclusion detectConclusion(GameState<T, P> gameState) {
        // No need for further computation if no king is in check
        if (allKingsSafe(gameState)) return null;
        // Detect all possible moves for the checked player
        MoveLookup<T, P> moves = moveGenerator.generateMoves(gameState);
        // Could perhaps be faster to sort moves according to which ones are most likely to
        // prevent check since checking fewer moves is faster
        for (Move<T, P> move : moves) {
            // Simulate move
            gameState.makeMove(move);
            gameState.switchTurn();
            // Use withTurnSwitched to reverse turn change in move.makeOn(GameState)
            if (allKingsSafe(gameState)) {
                // Found a move that prevents check
                gameState.switchTurn();
                gameState.unmakeLastMove();
                return null;
            }
            gameState.switchTurn();
            gameState.unmakeLastMove();
        }
        // No safe moves found :(
        return new GameConclusion(GameConclusion.Winner.of(gameState.getColor()), "Checkmate");
    }

    private boolean allKingsSafe(GameState<T, P> gameState) {
        Color isWhiteTurn = gameState.getColor();
        // Get all kings
        List<T> kingTiles = gameState
                .getTilesWithTypeAndColour(kingType, isWhiteTurn);
        if (kingTiles.isEmpty()) return true; // No kings => no check
        // withTurnSwitched to generate moves for the opposite player
        gameState.switchTurn();
        MoveLookup<T, P> opponentMoves = moveGenerator.generateMoves(gameState);
        gameState.switchTurn();

        for (T tile : kingTiles) {
            // Get king pos and check if opponent has any move to that square
            if (opponentMoves.containsMoveTo(tile)) return false;
        }
        return true; // No check found, everyone's happy
    }
}
