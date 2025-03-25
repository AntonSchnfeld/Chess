package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.move.MoveCollection;

import java.util.List;

public abstract class AbstractGenerativeMoveRule<T extends PieceType> implements GenerativeMoveRule<T> {
    private final T type;

    public AbstractGenerativeMoveRule(T type) {
        this.type = type;
    }

    protected abstract void generatePieceMoves(GameState<T> gameState,
                                               Square square,
                                               MoveCollection<T> moves);

    @Override
    public MoveCollection<T> generateMoves(GameState<T> gameState) {
        MoveCollection<T> moves = new MoveCollection<>();

        // Retrieve all knights belonging to the current player
        List<Square> knightSquares = gameState
                .getSquaresWithTypeAndColour(type, gameState.isWhiteTurn());

        for (Square square : knightSquares) generatePieceMoves(gameState, square, moves);

        return moves;
    }
}
