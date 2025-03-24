package de.schoenfeld.chess.rules.restrictive;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CastlingComponent;
import de.schoenfeld.chess.rules.MoveGenerator;

import java.lang.runtime.SwitchBootstraps;
import java.util.Iterator;
import java.util.List;

public class NoCastlingThroughCheckRule implements RestrictiveMoveRule<StandardPieceType> {
    private final MoveGenerator<StandardPieceType> moveGenerator;

    public NoCastlingThroughCheckRule(MoveGenerator<StandardPieceType> moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    @Override
    public void filterMoves(MoveCollection<StandardPieceType> moves, GameState<StandardPieceType> gameState) {
        Iterator<Move<StandardPieceType>> iterator = moves.iterator();

        while (iterator.hasNext()) {
            Move<StandardPieceType> move = iterator.next();

            if (!move.hasComponent(CastlingComponent.class)) continue;

            Square kingFrom = move.from();
            Square kingTo = move.to();
            List<Square> intermediatePositions = getIntermediateKingPositions(kingFrom, kingTo);

            if (isAnyPositionAttacked(intermediatePositions, gameState)) {
                iterator.remove();
            }
        }
    }

    private List<Square> getIntermediateKingPositions(Square from, Square to) {
        int direction = Integer.compare(to.x(), from.x()); // Only horizontal movement
        return List.of(
                new Square(from.x() + direction, from.y()),
                new Square(from.x() + 2 * direction, from.y()) // King's destination
        );
    }

    private boolean isAnyPositionAttacked(List<Square> squares,
                                          GameState<StandardPieceType> gameState) {
        GameState<StandardPieceType> enemyState = gameState
                .withIsWhiteTurn(!gameState.isWhiteTurn());
        MoveCollection<StandardPieceType> opponentMoves = moveGenerator.generateMoves(enemyState);

        for (Move<StandardPieceType> move : opponentMoves) {
            if (squares.contains(move.to())) {
                return true;
            }
        }

        return false;
    }
}
