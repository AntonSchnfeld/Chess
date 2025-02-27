package de.schoenfeld.chess.logic.rule;

import de.schoenfeld.chess.data.ReadOnlyGameState;
import de.schoenfeld.chess.data.move.MoveCollection;

import java.util.ArrayList;
import java.util.List;

public class RuleEngine {
    public static final RuleEngine DEFAULT_ENGINE = new RuleEngine(List.of(
            new CastlingRule(),
            new PromotionRule(),
            new EnPassantRule(),
            new CheckRule()
    ));

    private List<MoveRule> moveRules;

    public RuleEngine(List<MoveRule> moveRules) {
        this.moveRules = moveRules;
    }

    public RuleEngine() {
        this.moveRules = new ArrayList<>();
    }

    public void addMoveRule(MoveRule moveRule) {
        moveRules.add(moveRule);
    }

    public void applyRules(MoveCollection moves, ReadOnlyGameState gameState) {
        for (MoveRule moveRule : moveRules) {
            moveRule.apply(moves, gameState);
        }
    }
}
