package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.model.*;
import de.schoenfeld.chess.move.Move;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class CastlingComponentTest extends MoveComponentTest<StandardPieceType> {
    private ChessPiece<StandardPieceType> rook;
    private Square rookFrom;
    private Square rookTo;

    @Override
    @BeforeEach
    protected void setup() {
        gameState = new GameState<>(); // Use a real GameState

        // Create actual chess pieces
        piece = new ChessPiece<>(StandardPieceType.KING, true);
        rook = new ChessPiece<>(StandardPieceType.ROOK, true);

        // Define squares
        from = Square.of(4, 0); // King's starting position
        to = Square.of(6, 0);   // King's destination after castling
        rookFrom = Square.of(7, 0); // Rook's starting position
        rookTo = Square.of(5, 0);   // Rook's destination after castling

        // Place pieces on board
        gameState.setPieceAt(piece, from);
        gameState.setPieceAt(rook, rookFrom);

        // Create the move and component
        move = Move.of(piece, from, to);
        moveComponent = new CastlingComponent(rook, rookFrom, rookTo);
    }

    @Override
    protected void verifyComponentExecuted(GameState<StandardPieceType> gameState, Move<StandardPieceType> move) {
        // Check if both king and rook have moved
        assertEquals(piece, gameState.getPieceAt(to), "King should be at the target position.");
        assertEquals(rook, gameState.getPieceAt(rookTo), "Rook should have moved to its castling position.");

        // Ensure original squares are now empty
        assertNull(gameState.getPieceAt(from), "King's original square should be empty.");
        assertNull(gameState.getPieceAt(rookFrom), "Rook's original square should be empty.");
    }

    @Override
    protected void verifyComponentUndone(GameState<StandardPieceType> gameState, Move<StandardPieceType> move) {
        // Check if both king and rook returned correctly
        assertEquals(piece, gameState.getPieceAt(from), "King should be back at its original position.");
        assertEquals(rook, gameState.getPieceAt(rookFrom), "Rook should be back at its original position.");

        // Ensure castling target squares are now empty
        assertNull(gameState.getPieceAt(to), "King's castling destination should be empty after undo.");
        assertNull(gameState.getPieceAt(rookTo), "Rook's castling destination should be empty after undo.");
    }
}
