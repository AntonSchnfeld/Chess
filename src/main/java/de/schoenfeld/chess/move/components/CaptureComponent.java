package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.model.ChessPiece;

public record CaptureComponent(ChessPiece capturedPiece) implements MoveComponent {

}
