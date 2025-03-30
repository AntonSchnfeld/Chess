package de.schoenfeld.chesskit.rules.restrictive;

import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.Square;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.CastlingComponent;
import de.schoenfeld.chesskit.rules.MoveGenerator;

import java.util.Iterator;
import java.util.List;

public class NoCastlingThroughCheckRule implements RestrictiveMoveRule<StandardPieceType> {
    private final MoveGenerator<StandardPieceType> moveGenerator;

    public NoCastlingThroughCheckRule(MoveGenerator<StandardPieceType> moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    @Override
    public void filterMoves(MoveLookup<StandardPieceType> moves, GameState<StandardPieceType> gameState) {
        Iterator<Move<StandardPieceType>> iterator = moves.iterator();
        boolean isWhiteTurn = gameState.isWhiteTurn();
        while (iterator.hasNext()) {
            Move<StandardPieceType> move = iterator.next();

            if (!move.hasComponent(CastlingComponent.class)) continue;

            Square kingFrom = move.from();
            Square kingTo = move.to();
            List<Square> intermediatePositions = getIntermediateKingPositions(kingFrom, kingTo);

            if (isAnyPositionAttacked(intermediatePositions, gameState, isWhiteTurn)) {
                iterator.remove();
            }
        }
    }

    private List<Square> getIntermediateKingPositions(Square from, Square to) {
        int direction = Integer.compare(to.x(), from.x());
        return List.of(
                new Square(from.x() + direction, from.y()), // Feld, das König überquert
                to // direktes Zielfeld
        );
    }

    private boolean isAnyPositionAttacked(List<Square> squares,
                                          GameState<StandardPieceType> gameState,
                                          boolean isWhiteTurn) {
        gameState.switchTurn(); // zum Gegner wechseln
        MoveLookup<StandardPieceType> opponentMoves = moveGenerator.generateMoves(gameState);
        gameState.switchTurn(); // zurückwechseln zur ursprünglichen Partei

        for (Move<StandardPieceType> move : opponentMoves) {
            if (squares.contains(move.to())) {
                return true;
            }
        }

        return false;
    }
}
