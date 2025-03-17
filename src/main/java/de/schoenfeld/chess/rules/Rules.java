package de.schoenfeld.chess.rules;

import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.gameend.CheckMateRule;
import de.schoenfeld.chess.rules.gameend.GameEndRule;
import de.schoenfeld.chess.rules.gameend.InsufficientMaterialRule;
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

public record Rules(List<GenerativeMoveRule> generativeMoveRules, List<RestrictiveMoveRule> restrictiveMoveRules,
                    List<GameEndRule> gameEndRules) implements MoveGenerator {
    public static final Rules DEFAULT;

    static {
        var generativeMoveRules = List.of(
                new PawnMoveRule(),
                new KnightMoveRule(),
                new BishopMoveRule(),
                new RookMoveRule(),
                new QueenMoveRule(),
                new KingMoveRule()
        );
        MoveGenerator moveGenerator = new SimpleMoveGenerator(generativeMoveRules);
        var restrictiveMoveRules = List.of(
                new FriendlyFireRule(),
                new CheckRule(moveGenerator),
                new NoCastlingThroughCheckRule(moveGenerator)
        );
        var gameEndRules = List.of(
                new CheckMateRule(moveGenerator),
                new InsufficientMaterialRule()
        );
        DEFAULT = new Rules(generativeMoveRules, restrictiveMoveRules, gameEndRules);
    }

    public Rules(List<GenerativeMoveRule> generativeMoveRules,
                 List<RestrictiveMoveRule> restrictiveMoveRules,
                 List<GameEndRule> gameEndRules) {
        if (generativeMoveRules == null) throw new NullPointerException("generativeMoveRules");
        if (restrictiveMoveRules == null) throw new NullPointerException("restrictiveMoveRules");
        if (gameEndRules == null) throw new NullPointerException("gameEndRules");

        this.generativeMoveRules = List.copyOf(generativeMoveRules);
        this.restrictiveMoveRules = List.copyOf(restrictiveMoveRules);
        this.gameEndRules = List.copyOf(gameEndRules);
    }

    public Optional<GameConclusion> detectGameEndCause(GameState gameState) {
        for (var rule : gameEndRules) {
            var cause = rule.detectGameEndCause(gameState);
            if (cause.isPresent()) return cause;
        }
        return Optional.empty();
    }

    public MoveCollection generateMoves(GameState gameState) {
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

    public Rules withGenerativeMoveRules(List<GenerativeMoveRule> generativeMoveRules) {
        return new Rules(generativeMoveRules, restrictiveMoveRules, gameEndRules);
    }

    public Rules withRestrictiveMoveRules(List<RestrictiveMoveRule> restrictiveMoveRules) {
        return new Rules(generativeMoveRules, restrictiveMoveRules, gameEndRules);
    }

    public Rules withGameEndRules(List<GameEndRule> gameEndRules) {
        return new Rules(generativeMoveRules, restrictiveMoveRules, gameEndRules);
    }

    public Rules withGenerativeMoveRule(GenerativeMoveRule generativeMoveRule) {
        return new Rules(List.of(generativeMoveRule), restrictiveMoveRules, gameEndRules);
    }

    public Rules withRestrictiveMoveRule(RestrictiveMoveRule restrictiveMoveRule) {
        return new Rules(generativeMoveRules, List.of(restrictiveMoveRule), gameEndRules);
    }

    public Rules withGameEndRule(GameEndRule gameEndRule) {
        return new Rules(generativeMoveRules, restrictiveMoveRules, List.of(gameEndRule));
    }

    public Rules withoutGenerativeMoveRule(Class<? extends GenerativeMoveRule> clazz) {
        return new Rules(generativeMoveRules
                .stream()
                .filter(rule -> !clazz.isInstance(rule))
                .toList(), restrictiveMoveRules, gameEndRules);
    }

    public Rules withoutRestrictiveMoveRule(Class<? extends RestrictiveMoveRule> clazz) {
        return new Rules(generativeMoveRules, restrictiveMoveRules
                .stream()
                .filter(rule -> !clazz.isInstance(rule))
                .toList(), gameEndRules);
    }

    public Rules withoutGameEndRule(Class<? extends GameEndRule> clazz) {
        return new Rules(generativeMoveRules, restrictiveMoveRules, gameEndRules
                .stream()
                .filter(rule -> !clazz.isInstance(rule))
                .toList());
    }

    public Rules withoutGenerativeMoveRules(List<Class<? extends GenerativeMoveRule>> classes) {
        return new Rules(generativeMoveRules
                .stream()
                .filter(rule -> classes
                        .stream()
                        .noneMatch(clazz -> clazz.isInstance(rule)))
                .toList(), restrictiveMoveRules, gameEndRules);
    }

    public Rules withoutRestrictiveMoveRules(List<Class<? extends RestrictiveMoveRule>> classes) {
        return new Rules(generativeMoveRules, restrictiveMoveRules
                .stream()
                .filter(rule -> classes
                        .stream()
                        .noneMatch(clazz -> clazz.isInstance(rule)))
                .toList(), gameEndRules);
    }

    public Rules withoutGameEndRules(List<Class<? extends GameEndRule>> classes) {
        return new Rules(generativeMoveRules, restrictiveMoveRules, gameEndRules
                .stream()
                .filter(rule -> classes
                        .stream()
                        .noneMatch(clazz -> clazz.isInstance(rule)))
                .toList());
    }
}
