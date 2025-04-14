package de.schoenfeld.chesskit.model;

public enum StandardPieceType implements PieceType {
    PAWN("P", 100),
    KNIGHT("N", 320),
    BISHOP("B", 330),
    ROOK("R", 500),
    QUEEN("Q", 900),
    KING("K", 20000);

    private final String symbol;
    private final int value;

    StandardPieceType(String symbol, int value) {
        this.symbol = symbol;
        this.value = value;
    }

    public String symbol() {
        return symbol;
    }

    public int value() {
        return value;
    }
}