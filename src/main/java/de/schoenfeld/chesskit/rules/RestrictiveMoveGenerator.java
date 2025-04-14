package de.schoenfeld.chesskit.rules;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.generative.GenerativeMoveRule;
import de.schoenfeld.chesskit.rules.restrictive.RestrictiveMoveRule;

import java.util.List;

public class RestrictiveMoveGenerator<T extends Tile, P extends PieceType> implements MoveGenerator<T, P> {
    private final List<RestrictiveMoveRule<T, P>> restrictiveMoveRules;
    private final List<GenerativeMoveRule<T, P>> generativeMoveRules;

    public RestrictiveMoveGenerator(List<GenerativeMoveRule<T, P>> generativeMoveRules,
                                    List<RestrictiveMoveRule<T, P>> restrictiveMoveRules) {
        this.restrictiveMoveRules = List.copyOf(restrictiveMoveRules);
        this.generativeMoveRules = List.copyOf(generativeMoveRules);
    }

    @Override
    public MoveLookup<T, P> generateMoves(GameState<T, P> gameState) {
        MoveLookup<T, P> moves = generatePseudoLegalMoves(gameState);
        filterPseudoLegalMoves(moves, gameState);
        return moves;
    }

    @Override
    public MoveLookup<T, P> generatePseudoLegalMoves(GameState<T, P> gameState) {
        MoveLookup<T, P> moves = new MoveLookup<>();
        generatePseudoLegalMoves(gameState, moves);
        return moves;
    }

    @Override
    public void generatePseudoLegalMoves(GameState<T, P> gameState, MoveLookup<T, P> moves) {
        if (!moves.isEmpty()) moves.clear();
        for (GenerativeMoveRule<T, P> rule : generativeMoveRules)
            rule.generateMoves(gameState, moves);
    }

    @Override
    public void generateMoves(GameState<T, P> gameState, MoveLookup<T, P> moves) {
        generatePseudoLegalMoves(gameState, moves);
        filterPseudoLegalMoves(moves, gameState);
    }

    @Override
    public void filterPseudoLegalMoves(MoveLookup<T, P> moves, GameState<T, P> gameState) {
        for (RestrictiveMoveRule<T, P> rule : restrictiveMoveRules)
            rule.filterMoves(moves, gameState);
    }
}
