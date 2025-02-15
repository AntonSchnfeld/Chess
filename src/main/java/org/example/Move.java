package org.example;

import org.example.pieces.ChessPiece;

public record Move(ChessPiece movingPiece, Position startPosition, Position endPosition) {
    public Move(Builder builder) {
        this(builder.movingPiece, builder.startPosition, builder.endPosition);
    }

    public static class Builder {
        private ChessPiece movingPiece;
        private Position startPosition;
        private Position endPosition;

        public Builder movingPiece(ChessPiece movingPiece) {
            this.movingPiece = movingPiece;
            return this;
        }

        public Builder startPosition(Position startPosition) {
            this.startPosition = startPosition;
            return this;
        }

        public Builder endPosition(Position endPosition) {
            this.endPosition = endPosition;
            return this;
        }

        public Move build() {
            if (movingPiece == null) {
                throw new IllegalStateException("Moving piece must not be null.");
            }
            if (startPosition == null) {
                throw new IllegalStateException("Start position must not be null.");
            }
            if (endPosition == null) {
                throw new IllegalStateException("End position must not be null.");
            }

            return new Move(this);
        }
    }
}
