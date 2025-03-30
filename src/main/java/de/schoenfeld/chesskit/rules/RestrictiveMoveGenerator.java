package de.schoenfeld.chesskit.rules;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.generative.GenerativeMoveRule;
import de.schoenfeld.chesskit.rules.restrictive.RestrictiveMoveRule;

import java.util.List;

public class RestrictiveMoveGenerator<T extends PieceType> implements MoveGenerator<T> {
    private final List<RestrictiveMoveRule<T>> restrictiveMoveRules;
    private final List<GenerativeMoveRule<T>> generativeMoveRules;

    public RestrictiveMoveGenerator(List<GenerativeMoveRule<T>> generativeMoveRules, List<RestrictiveMoveRule<T>> restrictiveMoveRules) {
        this.restrictiveMoveRules = List.copyOf(restrictiveMoveRules);
        this.generativeMoveRules = List.copyOf(generativeMoveRules);
    }

    @Override
    public MoveLookup<T> generateMoves(GameState<T> gameState) {
        MoveLookup<T> moves = new MoveLookup<>();

        for (GenerativeMoveRule<T> rule : generativeMoveRules)
            moves.addAll(rule.generateMoves(gameState));

        for (RestrictiveMoveRule<T> rule : restrictiveMoveRules)
            rule.filterMoves(moves, gameState);

        return moves;
    }
}
