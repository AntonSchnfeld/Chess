package de.schoenfeld.chesskit.move.components;

import de.schoenfeld.chesskit.board.MapChessBoard;
import de.schoenfeld.chesskit.board.Square8x8ChessBoardBounds;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.rules.Rules;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CastlingComponentTest extends MoveComponentTest<Square8x8, StandardPieceType> {
    private ChessPiece<StandardPieceType> rook;
    private Square8x8 rookFrom;
    private Square8x8 rookTo;

    @Override
    @BeforeEach
    protected void setup() {
        gameState = new GameState<>(new MapChessBoard<>(new Square8x8ChessBoardBounds()), Rules.standard());
        // Use a real GameState

        // Create actual chess pieces
        piece = new ChessPiece<>(StandardPieceType.KING, Color.WHITE);
        rook = new ChessPiece<>(StandardPieceType.ROOK, Color.WHITE);

        // Define squares
        from = Square8x8.of(4, 0); // King's starting position
        to = Square8x8.of(6, 0);   // King's destination after castling
        rookFrom = Square8x8.of(7, 0); // Rook's starting position
        rookTo = Square8x8.of(5, 0);   // Rook's destination after castling

        // Place pieces on board
        gameState.setPieceAt(from, piece);
        gameState.setPieceAt(rookFrom, rook);

        // Create the move and component
        moveComponent = new CastlingComponent<>(rookFrom, rookTo);
        move = Move.of(piece, from, to, moveComponent);
    }

    @Override
    protected void verifyComponentExecuted(GameState<Square8x8, StandardPieceType> gameState,
                                           Move<Square8x8, StandardPieceType> move) {
        // Check if both king and rook have moved
        assertEquals(piece, gameState.getPieceAt(to), "King should be at the target position.");
        assertEquals(rook, gameState.getPieceAt(rookTo), "Rook should have moved to its castling position.");

        // Ensure the original squares are now empty
        assertNull(gameState.getPieceAt(from), "King's original square should be empty.");
        assertNull(gameState.getPieceAt(rookFrom), "Rook's original square should be empty.");
    }

    @Override
    protected void verifyComponentUndone(GameState<Square8x8, StandardPieceType> gameState,
                                         Move<Square8x8, StandardPieceType> move) {
        // Check if both king and rook returned correctly
        assertEquals(piece, gameState.getPieceAt(from), "King should be back at its original position.");
        assertEquals(rook, gameState.getPieceAt(rookFrom), "Rook should be back at its original position.");

        // Ensure castling target squares are now empty
        assertNull(gameState.getPieceAt(to), "King's castling destination should be empty after undo.");
        assertNull(gameState.getPieceAt(rookTo), "Rook's castling destination should be empty after undo.");
    }
}
