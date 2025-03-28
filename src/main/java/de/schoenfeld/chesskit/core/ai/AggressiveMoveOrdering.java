package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.components.CaptureComponent;

import java.util.Map;

public class AggressiveMoveOrdering implements MoveOrderingHeuristic<StandardPieceType> {

    // Material values for standard chess pieces (modify if custom pieces exist)
    private static final Map<PieceType, Integer> MATERIAL_VALUES = Map.of(
            StandardPieceType.PAWN, 100,
            StandardPieceType.KNIGHT, 300,
            StandardPieceType.BISHOP, 320,
            StandardPieceType.ROOK, 500,
            StandardPieceType.QUEEN, 900,
            StandardPieceType.KING, 10_000 // Arbitrary high value for king
    );

    @Override
    public int applyAsInt(Move<StandardPieceType> move) {
        int score = 0;

        ChessPiece<StandardPieceType> movedPiece = move.movedPiece();

        CaptureComponent<StandardPieceType> captureComponent = move.getComponent(CaptureComponent.class);
        ChessPiece<StandardPieceType> capturedPiece = null;
        if (captureComponent != null)
            capturedPiece = captureComponent.capturedPiece();

        // 1. Prioritize Captures (Most Valuable Victim - Least Valuable Attacker heuristic)
        if (capturedPiece != null) {
            int victimValue = MATERIAL_VALUES.getOrDefault(capturedPiece.pieceType(), 0);
            int attackerValue = MATERIAL_VALUES.getOrDefault(movedPiece.pieceType(), 1); // Avoid division by zero
            score += (victimValue - attackerValue) * 10; // Weighted to emphasize captures
        }

        // 2. Prioritize Moves That Put the Opponent in Check
        CaptureComponent<StandardPieceType> component = move.getComponent(CaptureComponent.class);
        boolean isCheck = component != null
                && component.capturedPiece().pieceType() == StandardPieceType.KING;
        if (isCheck) {
            score += 500;
        }

        // 3. Encourage Development (favor first moves of minor pieces)
        if (!movedPiece.hasMoved() && isMinorPiece(movedPiece)) {
            score += 50;
        }

        // 4. Encourage Pawn Advancement (especially toward promotion)
        if (movedPiece.pieceType().equals(StandardPieceType.PAWN)) {
            int rankProgress = move.to().y(); // Higher rank = closer to promotion
            score += rankProgress * 5;
        }

        return score;
    }

    private boolean isMinorPiece(ChessPiece<StandardPieceType> piece) {
        return piece.pieceType().equals(StandardPieceType.KNIGHT) || piece.pieceType().equals(StandardPieceType.BISHOP);
    }
}
