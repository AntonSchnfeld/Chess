package de.schoenfeld.chesskit.rules;

import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.events.GameConclusion;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.gameend.*;
import de.schoenfeld.chesskit.rules.generative.*;
import de.schoenfeld.chesskit.rules.generative.sliding.BishopMoveRule;
import de.schoenfeld.chesskit.rules.generative.sliding.QueenMoveRule;
import de.schoenfeld.chesskit.rules.generative.sliding.RookMoveRule;
import de.schoenfeld.chesskit.rules.restrictive.CheckRule;
import de.schoenfeld.chesskit.rules.restrictive.FriendlyFireRule;
import de.schoenfeld.chesskit.rules.restrictive.NoCastlingThroughCheckRule;
import de.schoenfeld.chesskit.rules.restrictive.RestrictiveMoveRule;

import java.util.List;

public record Rules<T extends Tile, P extends PieceType>(List<GenerativeMoveRule<T, P>> generativeMoveRules,
                                                         List<RestrictiveMoveRule<T, P>> restrictiveMoveRules,
                                                         List<GameConclusionDetector<T, P>> gameConclusionDetectors)
        implements MoveGenerator<T, P> {
    private static final Rules<Square8x8, StandardPieceType> STANDARD = createStandard();

    public Rules(List<GenerativeMoveRule<T, P>> generativeMoveRules,
                 List<RestrictiveMoveRule<T, P>> restrictiveMoveRules,
                 List<GameConclusionDetector<T, P>> gameConclusionDetectors) {
        if (generativeMoveRules == null) throw new NullPointerException("generativeMoveRules");
        if (restrictiveMoveRules == null) throw new NullPointerException("restrictiveMoveRules");
        if (gameConclusionDetectors == null) throw new NullPointerException("gameEndRules");

        this.generativeMoveRules = List.copyOf(generativeMoveRules);
        this.restrictiveMoveRules = List.copyOf(restrictiveMoveRules);
        this.gameConclusionDetectors = List.copyOf(gameConclusionDetectors);
    }

    public static Rules<Square8x8, StandardPieceType> standard() {
        return STANDARD;
    }

    private static Rules<Square8x8, StandardPieceType> createStandard() {
        List<GenerativeMoveRule<Square8x8, StandardPieceType>> generativeMoveRules = List.of(
                PawnMoveRule.standard(),
                KnightMoveRule.standard(),
                BishopMoveRule.standard(),
                RookMoveRule.standard(),
                QueenMoveRule.standard(),
                KingMoveRule.standard(),
                CastlingRule.standard(),
                EnPassantRule.standard()
        );
        List<RestrictiveMoveRule<Square8x8, StandardPieceType>> restrictiveMoveRules = List.of(
                FriendlyFireRule.standard(),
                NoCastlingThroughCheckRule.standard(),
                CheckRule.standard()
        );
        List<GameConclusionDetector<Square8x8, StandardPieceType>> gameEndRules = List.of(
                NoPiecesDetector.standard(),
                InsufficientMaterialDetector.standard(),
                StaleMateDetector.standard(),
                CheckMateDetector.standard()
        );
        return new Rules<>(generativeMoveRules,
                restrictiveMoveRules,
                gameEndRules);
    }

    public GameConclusion detectConclusion(GameState<T, P> gameState) {
        for (GameConclusionDetector<T, P> rule : gameConclusionDetectors) {
            GameConclusion cause = rule.detectConclusion(gameState);
            if (cause != null) return cause;
        }
        return null;
    }

    public MoveLookup<T, P> generateMoves(GameState<T, P> gameState) {
        MoveLookup<T, P> moves = new MoveLookup<>();
        generateMoves(gameState, moves);
        return moves;
    }

    public MoveLookup<T, P> generatePseudoLegalMoves(GameState<T, P> gameState) {
        MoveLookup<T, P> moves = new MoveLookup<>();
        generatePseudoLegalMoves(gameState, moves);
        return moves;
    }

    public void generatePseudoLegalMoves(GameState<T, P> gameState, MoveLookup<T, P> moves) {
        if (!moves.isEmpty()) moves.clear();
        for (GenerativeMoveRule<T, P> rule : generativeMoveRules)
            rule.generateMoves(gameState, moves);
    }

    public void generateMoves(GameState<T, P> gameState, MoveLookup<T, P> moves) {
        generatePseudoLegalMoves(gameState, moves);
        filterPseudoLegalMoves(moves, gameState);
    }

    public void filterPseudoLegalMoves(MoveLookup<T, P> moves, GameState<T, P> gameState) {
        for (RestrictiveMoveRule<T, P> rule : restrictiveMoveRules)
            rule.filterMoves(moves, gameState);
    }
}
