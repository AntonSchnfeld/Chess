package de.schoenfeld.chess.data.move;

import de.schoenfeld.chess.Position;
import de.schoenfeld.chess.logic.piece.ChessPiece;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Move {
    private final Set<MoveComponent> components;
    private final ChessPiece movedPiece;
    private final Position from, to;

    private Move(Set<MoveComponent> components,
                 ChessPiece movedPiece, Position from, Position to) {
        this.components = components;
        this.movedPiece = movedPiece;
        this.from = from;
        this.to = to;
    }

    public static Move of(ChessPiece movedPiece, Position from, Position to,
                          Set<MoveComponent> components) {
        return new Move(Set.copyOf(components), movedPiece, from, to);
    }

    public static Move of(ChessPiece movedPiece, Position from, Position to,
                          MoveComponent... components) {
        Map<Class<? extends MoveComponent>, MoveComponent> componentsMap = new HashMap<>();
        return new Move(Set.of(components), movedPiece, from, to);
    }

    public ChessPiece movedPiece() {
        return movedPiece;
    }

    public Position from() {
        return from;
    }

    public Position to() {
        return to;
    }

    public <T extends MoveComponent> T getComponent(Class<T> clazz) {
        return components.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .orElseThrow();
    }

    public Set<MoveComponent> getComponents() {
        return Collections.unmodifiableSet(components);
    }

    public boolean hasComponent(Class<? extends MoveComponent> clazz) {
        return components.stream().anyMatch(clazz::isInstance);
    }

    public boolean isCapture() {
        return hasComponent(CaptureComponent.class);
    }

    public boolean isPromotion() {
        return hasComponent(PromotionComponent.class);
    }

    public boolean isCastling() {
        return hasComponent(CastlingComponent.class);
    }
}