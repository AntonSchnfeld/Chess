package de.schoenfeld.chess.rules;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.generative.GenerativeMoveRule;
import de.schoenfeld.chess.rules.restrictive.RestrictiveMoveRule;

import java.util.List;

public class RestrictiveMoveGenerator<T extends PieceType> implements MoveGenerator<T> {
    private final List<RestrictiveMoveRule<T>> restrictiveMoveRules;
    private final List<GenerativeMoveRule<T>> generativeMoveRules;

    public RestrictiveMoveGenerator(List<RestrictiveMoveRule<T>> restrictiveMoveRules,
                                    List<GenerativeMoveRule<T>> generativeMoveRules) {
        this.restrictiveMoveRules = List.copyOf(restrictiveMoveRules);
        this.generativeMoveRules = List.copyOf(generativeMoveRules);
    }

    @Override
    public MoveCollection generateMoves(GameState<T> gameState) {
        MoveCollection moves = new MoveCollection();

        for (GenerativeMoveRule<T> rule : generativeMoveRules)
            moves.addAll(rule.generateMoves(gameState));

        for (RestrictiveMoveRule<T> rule : restrictiveMoveRules)
            rule.filterMoves(moves, gameState);

        return moves;
    }
}
