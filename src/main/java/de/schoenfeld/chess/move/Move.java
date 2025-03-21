package de.schoenfeld.chess.move;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.move.components.CaptureComponent;
import de.schoenfeld.chess.move.components.CastlingComponent;
import de.schoenfeld.chess.move.components.MoveComponent;
import de.schoenfeld.chess.move.components.PromotionComponent;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class Move implements Serializable {
    @Serial
    private static final long serialVersionUID = 1921632465085309764L;
    private final List<MoveComponent> components;
    private final ChessPiece movedPiece;
    private final Square from, to;

    private Move(List<MoveComponent> components,
                 ChessPiece movedPiece, Square from, Square to) {
        this.components = components;
        this.movedPiece = movedPiece;
        this.from = from;
        this.to = to;
    }

    public static Move of(ChessPiece movedPiece, Square from, Square to,
                          List<MoveComponent> components) {
        return new Move(List.copyOf(components), movedPiece, from, to);
    }

    public static Move of(ChessPiece movedPiece, Square from, Square to,
                          MoveComponent... components) {
        Map<Class<? extends MoveComponent>, MoveComponent> componentsMap = new HashMap<>();
        return new Move(List.of(components), movedPiece, from, to);
    }

    public ChessPiece movedPiece() {
        return movedPiece;
    }

    public Square from() {
        return from;
    }

    public Square to() {
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

    public Move withFrom(Square from) {
        if (this.from.equals(from)) return this;
        return new Move(components, movedPiece, from, to);
    }

    public Move withTo(Square to) {
        if (this.to.equals(to)) return this;
        return new Move(components, movedPiece, from, to);
    }

    public Move withMovedPiece(ChessPiece movedPiece) {
        if (this.movedPiece.equals(movedPiece)) return this;
        return new Move(components, movedPiece, from, to);
    }

    public Move withoutComponent(Class<? extends MoveComponent> clazz) {
        if (!hasComponent(clazz)) return this;
        List<MoveComponent> newComponents = components.stream()
                .filter(c -> c.getClass().equals(clazz))
                .toList();
        return new Move(newComponents, movedPiece, from, to);
    }

    public GameState executeOn(GameState gameState) {
        gameState = gameState
                .withMoveHistory(gameState.moveHistory().withMoveRecorded(this));
        gameState = gameState
                .withChessBoard(gameState.chessBoard().withPieceMoved(from, to));

        for (MoveComponent component : components) {
            gameState = gameState.withChessBoard(component.executeOn(gameState, this));
        }
        return gameState.withTurnSwitched();
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