package de.schoenfeld.chess.rules;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.generative.GenerativeMoveRule;

import java.util.List;

public record SimpleMoveGenerator(List<GenerativeMoveRule> rules) implements MoveGenerator {

    public SimpleMoveGenerator(List<GenerativeMoveRule> rules) {
        if (rules == null) throw new NullPointerException("rules");

        this.rules = List.copyOf(rules);
    }

    @Override
    public MoveCollection generateMoves(GameState gameState) {
        var moves = new MoveCollection();
        for (var rule : rules) {
            moves.addAll(rule.generateMoves(gameState));
        }
        return moves;
    }
}
