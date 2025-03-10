package de.schoenfeld.chess.core;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.*;

import java.util.List;

public class RuleEngine {
    public static final RuleEngine DEFAULT_ENGINE = new RuleEngine(List.of(
            new CastlingRule(),
            new PromotionRule(),
            new EnPassantRule(),
            new CheckRule()
    ));

    private final List<SpecialMoveRule> specialMoveRules;

    public RuleEngine(List<SpecialMoveRule> specialMoveRules) {
        this.specialMoveRules = List.copyOf(specialMoveRules);
    }

    public void applyRules(MoveCollection moves, GameState gameState) {
        for (SpecialMoveRule specialMoveRule : specialMoveRules) {
            specialMoveRule.apply(moves, gameState);
        }
    }
}
