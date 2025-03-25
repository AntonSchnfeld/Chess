package de.schoenfeld.chess.move.components;

import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.Move;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PromotionComponentTest extends MoveComponentTest<StandardPieceType> {
    private ChessPiece<StandardPieceType> promotedPiece;

    @Override
    @BeforeEach
    protected void setup() {
        gameState = new GameState<>(); // Use a real GameState

        // Create actual chess pieces
        piece = new ChessPiece<>(StandardPieceType.PAWN, true);  // Assume the player is white
        promotedPiece = new ChessPiece<>(StandardPieceType.QUEEN, true);  // Assume promotion to Queen

        // Define squares
        from = Square.of(1, 6); // Pawn's starting position (e.g., 2nd rank for white)
        to = Square.of(1, 7);   // Pawn's destination (promoting to the 8th rank)

        // Place pieces on board
        gameState.setPieceAt(from, piece);

        // Create the promotion component with the desired promoted piece
        moveComponent = new PromotionComponent(StandardPieceType.QUEEN);  // Promote to Queen
        move = Move.of(piece, from, to, moveComponent);
    }

    @Override
    protected void verifyComponentExecuted(GameState<StandardPieceType> gameState, Move<StandardPieceType> move) {
        // Check if the pawn was promoted to the new piece (Queen)
        assertEquals(promotedPiece, gameState.getPieceAt(to), "Pawn should be promoted to Queen at the target position.");

        // Ensure the original square is now empty
        assertNull(gameState.getPieceAt(from), "Original square of the pawn should be empty.");
    }

    @Override
    protected void verifyComponentUndone(GameState<StandardPieceType> gameState, Move<StandardPieceType> move) {
        // Check if the pawn is back at its original position
        assertEquals(piece, gameState.getPieceAt(from), "Pawn should be back at its original position.");

        // Ensure the promoted piece's square is now empty
        assertNull(gameState.getPieceAt(to), "Promoted piece's position should be empty after undo.");
    }
}
