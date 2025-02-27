package de.schoenfeld.chess.data.move;

import de.schoenfeld.chess.logic.piece.PieceType;

public record PromotionComponent(PieceType promotionTo) implements MoveComponent {
}
