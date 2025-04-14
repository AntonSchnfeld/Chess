package de.schoenfeld.chesskit.rules;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.generative.GenerativeMoveRule;

import java.util.List;

public record SimpleMoveGenerator<T extends Tile, P extends PieceType>(List<GenerativeMoveRule<T, P>> rules)
        implements MoveGenerator<T, P> {

    public SimpleMoveGenerator(List<GenerativeMoveRule<T, P>> rules) {
        if (rules == null) throw new NullPointerException("rules");

        this.rules = List.copyOf(rules);
    }

    @Override
    public void generateMoves(GameState<T, P> gameState, MoveLookup<T, P> moves) {
        if (!moves.isEmpty()) moves.clear();
        for (GenerativeMoveRule<T, P> rule : rules)
            rule.generateMoves(gameState, moves);
    }

    @Override
    public void generatePseudoLegalMoves(GameState<T, P> gameState, MoveLookup<T, P> moves) {
        generateMoves(gameState, moves);
    }

    @Override
    public MoveLookup<T, P> generateMoves(GameState<T, P> gameState) {
        MoveLookup<T, P> moves = new MoveLookup<>();
        generateMoves(gameState, moves);
        return moves;
    }

    @Override
    public MoveLookup<T, P> generatePseudoLegalMoves(GameState<T, P> gameState) {
        return generateMoves(gameState);
    }

    @Override
    public void filterPseudoLegalMoves(MoveLookup<T, P> moves, GameState<T, P> gameState) {
        // No implementation required here, this move generator implementation
        // only handles pseudo legal moves to begin with
    }
}
