package de.schoenfeld.chess.rules.restrictive;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CastlingComponent;
import de.schoenfeld.chess.rules.MoveGenerator;

import java.util.Iterator;
import java.util.List;

public class NoCastlingThroughCheckRule implements RestrictiveMoveRule {
    private final MoveGenerator moveGenerator;

    public NoCastlingThroughCheckRule(MoveGenerator moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    @Override
    public void filterMoves(MoveCollection moves, GameState gameState) {
        Iterator<Move> iterator = moves.iterator();

        while (iterator.hasNext()) {
            Move move = iterator.next();

            if (!move.hasComponent(CastlingComponent.class)) continue;

            var kingFrom = move.from();
            var kingTo = move.to();
            var intermediatePositions = getIntermediateKingPositions(kingFrom, kingTo);

            if (isAnyPositionAttacked(intermediatePositions, gameState)) {
                iterator.remove();
            }
        }
    }

    private List<Position> getIntermediateKingPositions(Position from, Position to) {
        int direction = Integer.compare(to.x(), from.x()); // Only horizontal movement
        return List.of(
                new Position(from.x() + direction, from.y()),
                new Position(from.x() + 2 * direction, from.y()) // King's destination
        );
    }

    private boolean isAnyPositionAttacked(List<Position> positions, GameState gameState) {
        var enemyState = gameState.withIsWhiteTurn(!gameState.isWhiteTurn());
        var opponentMoves = moveGenerator.generateMoves(enemyState);

        for (var move : opponentMoves) {
            if (positions.contains(move.to())) {
                return true;
            }
        }
        return false;
    }
}
