package de.schoenfeld.chesskit.rules;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveLookup;

public interface MoveGenerator<T extends Tile, P extends PieceType> {
    void generateMoves(GameState<T, P> gameState, MoveLookup<T, P> moves);

    void generatePseudoLegalMoves(GameState<T, P> gameState, MoveLookup<T, P> moves);

    MoveLookup<T, P> generateMoves(GameState<T, P> gameState);

    MoveLookup<T, P> generatePseudoLegalMoves(GameState<T, P> gameState);

    void filterPseudoLegalMoves(MoveLookup<T, P> moves, GameState<T, P> gameState);
}
