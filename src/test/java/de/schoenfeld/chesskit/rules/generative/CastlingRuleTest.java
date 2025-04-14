package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.board.MapChessBoard;
import de.schoenfeld.chesskit.board.Square8x8ChessBoardBounds;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.CastlingComponent;
import de.schoenfeld.chesskit.rules.Rules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CastlingRuleTest {
    private CastlingRule<StandardPieceType> castlingRule;
    private GameState<Square8x8, StandardPieceType> gameState;
    private MapChessBoard<Square8x8, StandardPieceType> board;

    @BeforeEach
    void setup() {
        castlingRule = CastlingRule.standard();
        board = new MapChessBoard<>(new Square8x8ChessBoardBounds());
        gameState = new GameState<>(board, Rules.standard()); // White to move
    }

    @Test
    void generateMoves_kingAndRookUnmoved_squaresEmpty_shouldAllowCastling() {
        Square8x8 kingPos = Square8x8.of(4, 0);
        Square8x8 kingSideRookPos = Square8x8.of(7, 0);
        Square8x8 queenSideRookPos = Square8x8.of(0, 0);

        // Place pieces on the board
        board.setPieceAt(kingPos, new ChessPiece<>(StandardPieceType.KING, Color.WHITE));
        board.setPieceAt(kingSideRookPos, new ChessPiece<>(StandardPieceType.ROOK, Color.WHITE));
        board.setPieceAt(queenSideRookPos, new ChessPiece<>(StandardPieceType.ROOK, Color.WHITE));

        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        castlingRule.generateMoves(gameState, moves);

        assertEquals(2, moves.size()); // Both king-side and queen-side castling should be possible
        assertTrue(moves
                .stream()
                .anyMatch(m -> m.hasComponent(CastlingComponent.class))
        );
    }

    @Test
    void generateMoves_kingOrRookMoved_shouldNotAllowCastling() {
        Square8x8 kingPos = Square8x8.of(4, 0);
        Square8x8 rookPos = Square8x8.of(7, 0);

        // Place pieces with 'hasMoved' set to true
        board.setPieceAt(kingPos, new ChessPiece<>(StandardPieceType.KING, Color.WHITE)); // King has moved
        board.setPieceAt(rookPos, new ChessPiece<>(StandardPieceType.ROOK, Color.BLACK));

        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        castlingRule.generateMoves(gameState, moves);

        assertTrue(moves.isEmpty()); // Castling should be blocked
    }

    @Test
    void generateMoves_squaresBetweenOccupied_shouldNotAllowCastling() {
        Square8x8 kingPos = Square8x8.of(4, 0);
        Square8x8 rookPos = Square8x8.of(7, 0);
        Square8x8 blockingPiecePos = Square8x8.of(5, 0);

        // Place pieces on the board
        board.setPieceAt(kingPos, new ChessPiece<>(StandardPieceType.KING, Color.BLACK));
        board.setPieceAt(rookPos, new ChessPiece<>(StandardPieceType.ROOK, Color.BLACK));
        board.setPieceAt(blockingPiecePos, new ChessPiece<>(StandardPieceType.PAWN, Color.BLACK));

        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        castlingRule.generateMoves(gameState, moves);

        assertTrue(moves.isEmpty()); // Castling shouldn't be allowed
    }
}
