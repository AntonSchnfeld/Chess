package de.schoenfeld.chesskit.rules;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveCollection;
import de.schoenfeld.chesskit.rules.generative.GenerativeMoveRule;

import java.util.List;

public record SimpleMoveGenerator<T extends PieceType>(List<GenerativeMoveRule<T>> rules)
        implements MoveGenerator<T> {

    public SimpleMoveGenerator(List<GenerativeMoveRule<T>> rules) {
        if (rules == null) throw new NullPointerException("rules");

        this.rules = List.copyOf(rules);
    }

    @Override
    public MoveCollection<T> generateMoves(GameState<T> gameState) {
        MoveCollection<T> moves = new MoveCollection<>();
        for (GenerativeMoveRule<T> rule : rules) {
            moves.addAll(rule.generateMoves(gameState));
        }
        return moves;
    }
}
