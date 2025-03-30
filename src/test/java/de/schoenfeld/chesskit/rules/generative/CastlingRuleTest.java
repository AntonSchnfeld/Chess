package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.board.MapChessBoard;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.Square;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.CastlingComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CastlingRuleTest {
    private CastlingRule castlingRule;
    private GameState<StandardPieceType> gameState;
    private MapChessBoard<StandardPieceType> board;

    @BeforeEach
    void setup() {
        castlingRule = new CastlingRule();
        board = new MapChessBoard<>();
        gameState = new GameState<>(board); // White to move
    }

    @Test
    void generateMoves_kingAndRookUnmoved_squaresEmpty_shouldAllowCastling() {
        Square kingPos = Square.of(4, 0);
        Square kingSideRookPos = Square.of(7, 0);
        Square queenSideRookPos = Square.of(0, 0);

        // Place pieces on the board
        board.setPieceAt(kingPos, new ChessPiece<>(StandardPieceType.KING, true));
        board.setPieceAt(kingSideRookPos, new ChessPiece<>(StandardPieceType.ROOK, true));
        board.setPieceAt(queenSideRookPos, new ChessPiece<>(StandardPieceType.ROOK, true));

        MoveLookup<StandardPieceType> moves = castlingRule.generateMoves(gameState);

        assertEquals(2, moves.size()); // Both king-side and queen-side castling should be possible
        assertTrue(moves.stream().anyMatch(m -> m.hasComponent(CastlingComponent.class)));
    }

    @Test
    void generateMoves_kingOrRookMoved_shouldNotAllowCastling() {
        Square kingPos = Square.of(4, 0);
        Square rookPos = Square.of(7, 0);

        // Place pieces with 'hasMoved' set to true
        board.setPieceAt(kingPos, new ChessPiece<>(StandardPieceType.KING, true)); // King has moved
        board.setPieceAt(rookPos, new ChessPiece<>(StandardPieceType.ROOK, false));

        MoveLookup<StandardPieceType> moves = castlingRule.generateMoves(gameState);

        assertTrue(moves.isEmpty()); // Castling should be blocked
    }

    @Test
    void generateMoves_squaresBetweenOccupied_shouldNotAllowCastling() {
        Square kingPos = Square.of(4, 0);
        Square rookPos = Square.of(7, 0);
        Square blockingPiecePos = Square.of(5, 0);

        // Place pieces on the board
        board.setPieceAt(kingPos, new ChessPiece<>(StandardPieceType.KING, false));
        board.setPieceAt(rookPos, new ChessPiece<>(StandardPieceType.ROOK, false));
        board.setPieceAt(blockingPiecePos, new ChessPiece<>(StandardPieceType.PAWN, false)); // Blocked path

        MoveLookup<StandardPieceType> moves = castlingRule.generateMoves(gameState);

        assertTrue(moves.isEmpty()); // Castling should not be allowed
    }
}
