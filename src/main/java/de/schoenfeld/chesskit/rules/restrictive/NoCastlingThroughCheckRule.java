package de.schoenfeld.chesskit.rules.restrictive;

import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.CastlingComponent;
import de.schoenfeld.chesskit.rules.MoveGenerator;
import de.schoenfeld.chesskit.rules.SimpleMoveGenerator;
import de.schoenfeld.chesskit.rules.generative.*;
import de.schoenfeld.chesskit.rules.generative.sliding.BishopMoveRule;
import de.schoenfeld.chesskit.rules.generative.sliding.QueenMoveRule;
import de.schoenfeld.chesskit.rules.generative.sliding.RookMoveRule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NoCastlingThroughCheckRule implements RestrictiveMoveRule<Square8x8, StandardPieceType> {
    private static final NoCastlingThroughCheckRule STANDARD = new NoCastlingThroughCheckRule(
            new SimpleMoveGenerator<>(List.of(
                    PawnMoveRule.standard(),
                    KnightMoveRule.standard(),
                    BishopMoveRule.standard(),
                    RookMoveRule.standard(),
                    QueenMoveRule.standard(),
                    KingMoveRule.standard(),
                    CastlingRule.standard(),
                    EnPassantRule.standard()
            ))
    );

    public static NoCastlingThroughCheckRule standard() {
        return STANDARD;
    }

    private final MoveGenerator<Square8x8, StandardPieceType> moveGenerator;

    public NoCastlingThroughCheckRule(MoveGenerator<Square8x8, StandardPieceType> moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    private List<Square8x8> getIntermediateKingPositions(Square8x8 from, Square8x8 to) {
        if (!(from != null && to != null)) return List.of();

        int direction = Integer.compare(to.x(), from.x());
        int fromX = from.x();
        int fromY = from.y();

        Square8x8 intermediate = Square8x8.of(fromX + direction, fromY);
        return List.of(intermediate, to);
    }

    private boolean isAnyPositionAttacked(List<Square8x8> positions,
                                          GameState<Square8x8, StandardPieceType> gameState) {
        gameState.switchTurn();
        MoveLookup<Square8x8, StandardPieceType> opponentMoves = moveGenerator.generatePseudoLegalMoves(gameState);
        gameState.switchTurn();

        for (Move<Square8x8, StandardPieceType> move : opponentMoves) {
            if (positions.contains(move.to())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void filterMoves(MoveLookup<Square8x8, StandardPieceType> moves,
                            GameState<Square8x8, StandardPieceType> gameState) {
        Iterator<Move<Square8x8, StandardPieceType>> iterator = moves.iterator();
        List<Move<Square8x8, StandardPieceType>> movesToRemove = new ArrayList<>();

        while (iterator.hasNext()) {
            Move<Square8x8, StandardPieceType> move = iterator.next();
            if (!move.hasComponent(CastlingComponent.class)) continue;

            Square8x8 kingFrom = move.from();
            Square8x8 kingTo = move.to();
            List<Square8x8> intermediatePositions = getIntermediateKingPositions(kingFrom, kingTo);

            if (isAnyPositionAttacked(intermediatePositions, gameState)) {
                movesToRemove.add(move);
            }
        }

        moves.removeAll(movesToRemove);
    }
}
