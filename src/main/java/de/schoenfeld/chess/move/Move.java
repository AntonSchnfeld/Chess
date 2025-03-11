package de.schoenfeld.chess.move;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.components.CaptureComponent;
import de.schoenfeld.chess.move.components.CastlingComponent;
import de.schoenfeld.chess.move.components.MoveComponent;
import de.schoenfeld.chess.move.components.PromotionComponent;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class Move implements Serializable {
    @Serial
    private static final long serialVersionUID = 4700273571919913977L;
    private final List<MoveComponent> components;
    private final ChessPiece movedPiece;
    private final Position from, to;

    private Move(List<MoveComponent> components,
                 ChessPiece movedPiece, Position from, Position to) {
        this.components = components;
        this.movedPiece = movedPiece;
        this.from = from;
        this.to = to;
    }

    public static Move of(ChessPiece movedPiece, Position from, Position to,
                          Set<MoveComponent> components) {
        return new Move(List.copyOf(components), movedPiece, from, to);
    }

    public static Move of(ChessPiece movedPiece, Position from, Position to,
                          MoveComponent... components) {
        Map<Class<? extends MoveComponent>, MoveComponent> componentsMap = new HashMap<>();
        return new Move(List.of(components), movedPiece, from, to);
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

    public List<MoveComponent> getComponents() {
        return components;
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

    public <T extends MoveComponent> Move withComponent(T component) {
        List<MoveComponent> newComponents = new ArrayList<>(this.components);
        newComponents.add(component);
        return new Move(newComponents, movedPiece, from, to);
    }

    public Move withFrom(Position from) {
        return new Move(components, movedPiece, from, to);
    }

    public Move withTo(Position to) {
        return new Move(components, movedPiece, from, to);
    }

    public Move withMovedPiece(ChessPiece movedPiece) {
        return new Move(components, movedPiece, from, to);
    }

    public Move withoutComponent(Class<? extends MoveComponent> clazz) {
        List<MoveComponent> newComponents = components.stream()
                .filter(c -> c.getClass().equals(clazz))
                .toList();
        return new Move(newComponents, movedPiece, from, to);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Move move = (Move) object;
        return Objects.equals(components, move.components) && Objects.equals(movedPiece, move.movedPiece) && Objects.equals(from, move.from) && Objects.equals(to, move.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(components, movedPiece, from, to);
    }

    @Override
    public String toString() {
        return "Move{" +
                "components=" + components +
                ", movedPiece=" + movedPiece +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}