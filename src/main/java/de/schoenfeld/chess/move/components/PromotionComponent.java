package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.model.PieceType;

public record PromotionComponent(PieceType promotionTo) implements MoveComponent {
}
