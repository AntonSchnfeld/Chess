package org.example;

public record ChessBoardDimensions(int rows, int columns) {
    public boolean contains(Position position) {
        return position.x() >= 0 &&
                rows > position.x() &&
                position.y() >= 0 &&
                columns > position.y();
    }
}
