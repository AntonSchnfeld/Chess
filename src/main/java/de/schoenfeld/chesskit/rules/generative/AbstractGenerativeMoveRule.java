package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.Square;
import de.schoenfeld.chesskit.move.MoveLookup;

import java.util.List;

public abstract class AbstractGenerativeMoveRule<T extends PieceType> implements GenerativeMoveRule<T> {
    private final T type;

    public AbstractGenerativeMoveRule(T type) {
        this.type = type;
    }

    protected abstract void generatePieceMoves(GameState<T> gameState,
                                               Square square,
                                               MoveLookup<T> moves);

    @Override
    public MoveLookup<T> generateMoves(GameState<T> gameState) {
        MoveLookup<T> moves = new MoveLookup<>();

        // Retrieve all knights belonging to the current player
        List<Square> knightSquares = gameState
                .getSquaresWithTypeAndColour(type, gameState.isWhiteTurn());

        for (Square square : knightSquares) generatePieceMoves(gameState, square, moves);

        return moves;
    }
}
