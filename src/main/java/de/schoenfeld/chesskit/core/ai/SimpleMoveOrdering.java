package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.board.tile.Square;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.components.CaptureComponent;
import de.schoenfeld.chesskit.move.components.CastlingComponent;
import de.schoenfeld.chesskit.move.components.PromotionComponent;

public class SimpleMoveOrdering implements MoveOrderingHeuristic<Square8x8, StandardPieceType> {
    @Override
    public int applyAsInt(Move<Square8x8, StandardPieceType> value) {
        int ordering = 0;

        // Bauernumwandlung
        if (value.hasComponent(PromotionComponent.class)) {
            ordering += value.getComponent(PromotionComponent.class).promotionTo().value();
        }

        // MVV-LVA captures
        if (value.hasComponent(CaptureComponent.class)) {
            CaptureComponent<Square8x8, StandardPieceType> captureComponent = value.getComponent(CaptureComponent.class);
            ordering += captureComponent.capturedPiece().pieceType().value()
                    - value.movedPiece().pieceType().value();
        }

        // Rochade
        if (value.hasComponent(CastlingComponent.class)) {
            ordering += 200;
        }

        // Zentralisierung
        ordering += 3 * getCentralizationBonus(value.to());

        // Figurenbasiswert
        ordering += value.movedPiece().pieceType().value();

        // Bauernbewegungen zur Beförderung hin begünstigen
        if (value.movedPiece().pieceType() == StandardPieceType.PAWN) {
            ordering += (value.movedPiece().color() == Color.WHITE) ?
                    value.to().y() * 10 : (7 - value.to().y()) * 10;
        }

        return ordering;
    }

    int getCentralizationBonus(Square8x8 position) {
        int[][] centralityBonus = {
                {0, 4, 8, 10, 10, 8, 4, 0},
                {4, 8, 12, 14, 14, 12, 8, 4},
                {8, 12, 16, 18, 18, 16, 12, 8},
                {10, 14, 18, 20, 20, 18, 14, 10},
                {10, 14, 18, 20, 20, 18, 14, 10},
                {8, 12, 16, 18, 18, 16, 12, 8},
                {4, 8, 12, 14, 14, 12, 8, 4},
                {0, 4, 8, 10, 10, 8, 4, 0}
        };
        return centralityBonus[position.x()][position.y()];
    }

}
