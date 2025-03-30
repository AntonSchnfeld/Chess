package de.schoenfeld.chesskit.move.components;

import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.Square;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;

public class EnPassantComponent extends CaptureComponent<StandardPieceType> {

    private final Square enPassantSquare;

    public EnPassantComponent(ChessPiece<StandardPieceType> capturedPiece, Square enPassantSquare) {
        super(capturedPiece);
        this.enPassantSquare = enPassantSquare;
    }

    @Override
    public void undoOn(GameState<StandardPieceType> gameState, Move<StandardPieceType> move) {
        gameState.setPieceAt(enPassantSquare, capturedPiece);
    }

    @Override
    public void executeOn(GameState<StandardPieceType> gameState, Move<StandardPieceType> move) {
        gameState.removePieceAt(enPassantSquare);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(getClass())) return false;

        EnPassantComponent other = (EnPassantComponent) o;

        return this.capturedPiece.equals(other.capturedPiece);
    }

    @Override
    public String toString() {
        return "EnPassantComponent{" +
                "capturedPiece=" + capturedPiece +
                '}';
    }
}
