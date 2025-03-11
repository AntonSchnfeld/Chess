package de.schoenfeld.chess.model;

public record Position(int x, int y) {

    public static Position of(int x, int y) {
        return new Position(x, y);
    }

    public Position offset(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }

    public Position offset(Position position) {
        if (position == null)
            throw new IllegalArgumentException("position must not be null");
        return new Position(position.x + x, position.y + y);
    }

    public String toAlgebraic() {
        return String.format("%c%d", 'a' + x, y + 1);
    }
}
