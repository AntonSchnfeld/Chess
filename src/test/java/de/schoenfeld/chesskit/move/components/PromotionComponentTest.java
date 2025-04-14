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

class PromotionComponentTest extends MoveComponentTest<Square8x8, StandardPieceType> {
    private ChessPiece<StandardPieceType> promotedPiece;

    @Override
    @BeforeEach
    protected void setup() {
        gameState = new GameState<>(new MapChessBoard<>(new Square8x8ChessBoardBounds()), Rules.standard());

        piece = new ChessPiece<>(StandardPieceType.PAWN, Color.WHITE);
        promotedPiece = new ChessPiece<>(StandardPieceType.QUEEN, Color.WHITE);

        from = Square8x8.of(1, 6);
        to = Square8x8.of(1, 7);

        gameState.setPieceAt(from, piece);

        moveComponent = new PromotionComponent<>(StandardPieceType.QUEEN);
        move = Move.of(piece, from, to, moveComponent);
    }

    @Override
    protected void verifyComponentExecuted(GameState<Square8x8, StandardPieceType> gameState,
                                           Move<Square8x8, StandardPieceType> move) {
        assertEquals(promotedPiece, gameState.getPieceAt(to), "Pawn should be promoted to Queen at the target position.");

        assertNull(gameState.getPieceAt(from), "Original square of the pawn should be empty.");
    }

    @Override
    protected void verifyComponentUndone(GameState<Square8x8, StandardPieceType> gameState,
                                         Move<Square8x8, StandardPieceType> move) {
        assertEquals(piece, gameState.getPieceAt(from), "Pawn should be back at its original position.");

        assertNull(gameState.getPieceAt(to), "Promoted piece's position should be empty after undo.");
    }
}
