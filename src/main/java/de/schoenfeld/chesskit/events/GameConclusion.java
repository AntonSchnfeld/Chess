package de.schoenfeld.chesskit.events;

public record GameConclusion(Winner winner, String description) {

    public boolean isDraw() {
        return winner == Winner.NONE;
    }

    public enum Winner {
        WHITE, BLACK, NONE;

        public static Winner of(boolean isWhite) {
            return isWhite ? WHITE : BLACK;
        }
    }
}