package de.schoenfeld.chesskit.rules;

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
import java.util.Optional;

public record Rules<T extends PieceType>(List<GenerativeMoveRule<T>> generativeMoveRules,
                                         List<RestrictiveMoveRule<T>> restrictiveMoveRules,
                                         List<GameConclusionRule<T>> gameConclusionRules)
        implements MoveGenerator<T> {
    private static final Rules<StandardPieceType> STANDARD = createStandard();

    public static Rules<StandardPieceType> standard() {
        return STANDARD;
    }

    private static Rules<StandardPieceType> createStandard() {
        List<GenerativeMoveRule<StandardPieceType>> generativeMoveRules = List.of(
                PawnMoveRule.standard(),
                KnightMoveRule.standard(),
                BishopMoveRule.standard(),
                RookMoveRule.standard(),
                QueenMoveRule.standard(),
                KingMoveRule.standard(),
                new CastlingRule(),
                new EnPassantRule()
        );
        MoveGenerator<StandardPieceType> moveGenerator = new SimpleMoveGenerator<>(generativeMoveRules);
        List<RestrictiveMoveRule<StandardPieceType>> restrictiveMoveRules = List.of(
                new FriendlyFireRule<>(),
                new NoCastlingThroughCheckRule(moveGenerator),
                new CheckRule(moveGenerator)
        );
        MoveGenerator<StandardPieceType> gameEndMoveGenerator =
                new RestrictiveMoveGenerator<>(generativeMoveRules, restrictiveMoveRules);
        List<GameConclusionRule<StandardPieceType>> gameEndRules = List.of(
                new NoPiecesRule<>(),
                new InsufficientMaterialRule(),
                new StaleMateRule(gameEndMoveGenerator),
                new CheckMateRule(gameEndMoveGenerator)
        );
        return new Rules<>(generativeMoveRules, restrictiveMoveRules, gameEndRules);
    }

    public Rules(List<GenerativeMoveRule<T>> generativeMoveRules,
                 List<RestrictiveMoveRule<T>> restrictiveMoveRules,
                 List<GameConclusionRule<T>> gameConclusionRules) {
        if (generativeMoveRules == null) throw new NullPointerException("generativeMoveRules");
        if (restrictiveMoveRules == null) throw new NullPointerException("restrictiveMoveRules");
        if (gameConclusionRules == null) throw new NullPointerException("gameEndRules");

        this.generativeMoveRules = List.copyOf(generativeMoveRules);
        this.restrictiveMoveRules = List.copyOf(restrictiveMoveRules);
        this.gameConclusionRules = List.copyOf(gameConclusionRules);
    }

    public Optional<GameConclusion> detectGameEndCause(GameState<T> gameState) {
        for (var rule : gameConclusionRules) {
            var cause = rule.detectGameEndCause(gameState);
            if (cause.isPresent()) return cause;
        }
        return Optional.empty();
    }

    public MoveLookup<T> generateMoves(GameState<T> gameState) {
        MoveLookup<T> moves = new MoveLookup<>();
        // Check if the game has ended
        if (detectGameEndCause(gameState).isPresent()) return moves;

        // Generate moves
        for (var rule : generativeMoveRules)
            moves.addAll(rule.generateMoves(gameState));

        // Filter moves
        for (var rule : restrictiveMoveRules)
            rule.filterMoves(moves, gameState);

        return moves;
    }
}
