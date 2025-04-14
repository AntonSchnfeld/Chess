package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.move.MoveLookup;

import java.util.List;

public abstract class AbstractGenerativeMoveRule<T extends Tile, P extends PieceType>
        implements GenerativeMoveRule<T, P> {
    private final P type;

    public AbstractGenerativeMoveRule(P type) {
        this.type = type;
    }

    protected abstract void generatePieceMoves(GameState<T, P> gameState,
                                               T tile,
                                               MoveLookup<T, P> moves);

    @Override
    public void generateMoves(GameState<T, P> gameState, MoveLookup<T, P> moves) {
        // Retrieve all knights belonging to the current player
        List<T> knightSquare8x8s = gameState
                .getTilesWithTypeAndColour(type, gameState.getColor());

        for (T tile : knightSquare8x8s) generatePieceMoves(gameState, tile, moves);
    }
}
