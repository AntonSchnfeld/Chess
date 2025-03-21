package de.schoenfeld.chess.rules;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.generative.GenerativeMoveRule;
import de.schoenfeld.chess.rules.restrictive.RestrictiveMoveRule;

import java.util.List;

public class RestrictiveMoveGenerator implements MoveGenerator {
    private final List<RestrictiveMoveRule> restrictiveMoveRules;
    private final List<GenerativeMoveRule> generativeMoveRules;

    public RestrictiveMoveGenerator(List<RestrictiveMoveRule> restrictiveMoveRules,
                               List<GenerativeMoveRule> generativeMoveRules) {
        this.restrictiveMoveRules = List.copyOf(restrictiveMoveRules);
        this.generativeMoveRules = List.copyOf(generativeMoveRules);
    }

    @Override
    public MoveCollection generateMoves(GameState gameState) {
        MoveCollection moves = new MoveCollection();

        for (GenerativeMoveRule rule : generativeMoveRules)
            moves.addAll(rule.generateMoves(gameState));

        for (RestrictiveMoveRule rule : restrictiveMoveRules)
            rule.filterMoves(moves, gameState);

        return moves;
    }
}
