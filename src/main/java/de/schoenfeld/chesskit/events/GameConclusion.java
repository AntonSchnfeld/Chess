package de.schoenfeld.chesskit.events;

import de.schoenfeld.chesskit.model.Color;

public record GameConclusion(Winner winner, String description) {

    public boolean isDraw() {
        return winner == Winner.NONE;
    }

    public enum Winner {
        WHITE, BLACK, NONE;

        public static Winner of(Color color) {
            return color == Color.WHITE ? WHITE : BLACK;
        }
    }
}