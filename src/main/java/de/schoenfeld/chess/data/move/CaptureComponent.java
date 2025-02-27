package de.schoenfeld.chess.data.move;

import de.schoenfeld.chess.logic.piece.ChessPiece;

public record CaptureComponent(ChessPiece capturedPiece) implements MoveComponent {

}
