package de.schoenfeld.chess.rules;

import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.gameend.*;
import de.schoenfeld.chess.rules.generative.GenerativeMoveRule;
import de.schoenfeld.chess.rules.generative.KingMoveRule;
import de.schoenfeld.chess.rules.generative.KnightMoveRule;
import de.schoenfeld.chess.rules.generative.PawnMoveRule;
import de.schoenfeld.chess.rules.generative.sliding.BishopMoveRule;
import de.schoenfeld.chess.rules.generative.sliding.QueenMoveRule;
import de.schoenfeld.chess.rules.generative.sliding.RookMoveRule;
import de.schoenfeld.chess.rules.restrictive.CheckRule;
import de.schoenfeld.chess.rules.restrictive.FriendlyFireRule;
import de.schoenfeld.chess.rules.restrictive.NoCastlingThroughCheckRule;
import de.schoenfeld.chess.rules.restrictive.RestrictiveMoveRule;

import java.util.List;
import java.util.Optional;

public record Rules<T extends PieceType>(List<GenerativeMoveRule<T>> generativeMoveRules,
                                         List<RestrictiveMoveRule<T>> restrictiveMoveRules,
                                         List<GameConclusionRule<T>> gameConclusionRules)
        implements MoveGenerator<T> {
    public static final Rules<StandardPieceType> DEFAULT;

    static {
        var generativeMoveRules = List.of(
                new PawnMoveRule(),
                new KnightMoveRule(),
                new BishopMoveRule(),
                new RookMoveRule(),
                new QueenMoveRule(),
                new KingMoveRule()
        );
        MoveGenerator<StandardPieceType> moveGenerator = new SimpleMoveGenerator<>(generativeMoveRules);
        var restrictiveMoveRules = List.of(
                new FriendlyFireRule<StandardPieceType>(),
                new CheckRule(moveGenerator),
                new NoCastlingThroughCheckRule(moveGenerator)
        );
        MoveGenerator<StandardPieceType> gameEndMoveGenerator =
                new RestrictiveMoveGenerator<>(restrictiveMoveRules, generativeMoveRules);
        var gameEndRules = List.of(
                new NoPiecesRule<StandardPieceType>(),
                new InsufficientMaterialRule(),
                new StaleMateRule(gameEndMoveGenerator),
                new CheckMateRule(gameEndMoveGenerator)
        );
        DEFAULT = new Rules<>(generativeMoveRules, restrictiveMoveRules, gameEndRules);
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

    public MoveCollection generateMoves(GameState<T> gameState) {
        var moves = new MoveCollection();
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

    public Rules<T> withGenerativeMoveRules(List<GenerativeMoveRule<T>> generativeMoveRules) {
        return new Rules<>(generativeMoveRules, restrictiveMoveRules, gameConclusionRules);
    }

    public Rules<T> withRestrictiveMoveRules(List<RestrictiveMoveRule<T>> restrictiveMoveRules) {
        return new Rules<>(generativeMoveRules, restrictiveMoveRules, gameConclusionRules);
    }

    public Rules<T> withGameEndRules(List<GameConclusionRule<T>> gameConclusionRules) {
        return new Rules<>(generativeMoveRules, restrictiveMoveRules, gameConclusionRules);
    }

    public Rules<T> withGenerativeMoveRule(GenerativeMoveRule<T> generativeMoveRule) {
        return new Rules<>(List.of(generativeMoveRule), restrictiveMoveRules, gameConclusionRules);
    }

    public Rules<T> withRestrictiveMoveRule(RestrictiveMoveRule<T> restrictiveMoveRule) {
        return new Rules<>(generativeMoveRules, List.of(restrictiveMoveRule), gameConclusionRules);
    }

    public Rules<T> withGameEndRule(GameConclusionRule<T> gameConclusionRule) {
        return new Rules<>(generativeMoveRules, restrictiveMoveRules, List.of(gameConclusionRule));
    }

    public Rules<T> withoutGenerativeMoveRule(Class<? extends GenerativeMoveRule<T>> clazz) {
        return new Rules<>(generativeMoveRules
                .stream()
                .filter(rule -> !clazz.isInstance(rule))
                .toList(), restrictiveMoveRules, gameConclusionRules);
    }

    public Rules<T> withoutRestrictiveMoveRule(Class<? extends RestrictiveMoveRule<T>> clazz) {
        return new Rules<>(generativeMoveRules, restrictiveMoveRules
                .stream()
                .filter(rule -> !clazz.isInstance(rule))
                .toList(), gameConclusionRules);
    }

    public Rules<T> withoutGameEndRule(Class<? extends GameConclusionRule<T>> clazz) {
        return new Rules<>(generativeMoveRules, restrictiveMoveRules, gameConclusionRules
                .stream()
                .filter(rule -> !clazz.isInstance(rule))
                .toList());
    }

    public Rules<T> withoutGenerativeMoveRules(List<Class<? extends GenerativeMoveRule<T>>> classes) {
        return new Rules<>(generativeMoveRules
                .stream()
                .filter(rule -> classes
                        .stream()
                        .noneMatch(clazz -> clazz.isInstance(rule)))
                .toList(), restrictiveMoveRules, gameConclusionRules);
    }

    public Rules<T> withoutRestrictiveMoveRules(List<Class<? extends RestrictiveMoveRule<T>>> classes) {
        return new Rules<>(generativeMoveRules, restrictiveMoveRules
                .stream()
                .filter(rule -> classes
                        .stream()
                        .noneMatch(clazz -> clazz.isInstance(rule)))
                .toList(), gameConclusionRules);
    }

    public Rules<T> withoutGameEndRules(List<Class<? extends GameConclusionRule<T>>> classes) {
        return new Rules<>(generativeMoveRules, restrictiveMoveRules, gameConclusionRules
                .stream()
                .filter(rule -> classes
                        .stream()
                        .noneMatch(clazz -> clazz.isInstance(rule)))
                .toList());
    }
}
