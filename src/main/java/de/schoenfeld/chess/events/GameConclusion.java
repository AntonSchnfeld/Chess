package de.schoenfeld.chess.events;

public record GameConclusion(Winner winner, String description) {

    public enum Winner {
        WHITE, BLACK, NONE;

        public static Winner of(boolean isWhite) {
            return isWhite ? WHITE : BLACK;
        }
    }

    public boolean isDraw() {
        return winner == Winner.NONE;
    }
}