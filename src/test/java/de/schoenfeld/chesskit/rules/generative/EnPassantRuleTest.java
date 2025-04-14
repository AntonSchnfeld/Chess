package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.board.MapChessBoard;
import de.schoenfeld.chesskit.board.Square8x8ChessBoardBounds;
import de.schoenfeld.chesskit.model.ChessPiece;
import de.schoenfeld.chesskit.model.Color;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.EnPassantComponent;
import de.schoenfeld.chesskit.rules.Rules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnPassantRuleTest {
    private EnPassantRule<StandardPieceType> tested;
    private GameState<Square8x8, StandardPieceType> gameState;
    private ChessPiece<StandardPieceType> pawn;
    private Square8x8 pawnPos;

    @BeforeEach
    public void setup() {
        tested = EnPassantRule.standard();
        gameState = new GameState<>(new MapChessBoard<>(new Square8x8ChessBoardBounds()), Rules.standard());

        pawn = new ChessPiece<>(StandardPieceType.PAWN, Color.WHITE);
        pawnPos = Square8x8.of(3, 3);

        gameState.setPieceAt(pawnPos, pawn);
    }

    @Test
    public void givenNoMoveHistory_whenGenerateMoves_thenNoEnPassant() {
        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        tested.generateMoves(gameState, moves);

        assertTrue(moves.isEmpty(), "En passant should not be possible without move history.");
    }

    @Test
    public void givenLastMoveNotPawn_whenGenerateMoves_thenNoEnPassant() {
        Move<Square8x8, StandardPieceType> nonPawnMove = Move.of(
                new ChessPiece<>(StandardPieceType.ROOK, Color.BLACK),
                Square8x8.of(2, 1),
                Square8x8.of(2, 3)
        );

        gameState.setPieceAt(nonPawnMove.to(), nonPawnMove.movedPiece());
        gameState.getMoveHistory().recordMove(nonPawnMove);

        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        tested.generateMoves(gameState, moves);

        assertTrue(moves.isEmpty(), "En passant should not be possible if the last move was not a pawn move.");
    }

    @Test
    public void givenLastMoveNotDoublePawnMove_whenGenerateMoves_thenNoEnPassant() {
        Move<Square8x8, StandardPieceType> pawnMove = Move.of(
                new ChessPiece<>(StandardPieceType.PAWN, Color.BLACK),
                Square8x8.of(2, 2),
                Square8x8.of(2, 3)
        );

        gameState.setPieceAt(pawnMove.to(), pawnMove.movedPiece());
        gameState.getMoveHistory().recordMove(pawnMove);

        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        tested.generateMoves(gameState, moves);

        assertTrue(moves.isEmpty(), "En passant should not be possible if the last pawn move was not a double move.");
    }

    @Test
    public void givenValidEnPassantForWhite_whenGenerateMoves_thenMoveIsGenerated() {
        // Weißer Bauer auf der korrekten En-Passant Position nach Gegner-Doppelschritt
        pawnPos = Square8x8.of(4, 4);
        pawn = new ChessPiece<>(StandardPieceType.PAWN, Color.WHITE);

        gameState.setPieceAt(pawnPos, pawn);

        Square8x8 enemyPawnStart = Square8x8.of(3, 6);
        Square8x8 enemyPawnEnd = Square8x8.of(3, 4);
        ChessPiece<StandardPieceType> enemyPawn = new ChessPiece<>(StandardPieceType.PAWN, Color.BLACK);
        gameState.setPieceAt(enemyPawnEnd, enemyPawn);

        Move<Square8x8, StandardPieceType> enemyPawnDoubleMove = Move.of(enemyPawn, enemyPawnStart, enemyPawnEnd);
        gameState.makeMove(enemyPawnDoubleMove); // Historie hinzufügen

        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        tested.generateMoves(gameState, moves);

        assertEquals(1, moves.size(), "Ein En-Passant-Zug muss generiert werden.");
        Move<Square8x8, StandardPieceType> move = moves.getFirst();
        assertTrue(move.hasComponent(EnPassantComponent.class), "Zug sollte eine EnPassantComponent haben.");
        assertEquals(Square8x8.of(3, 5), move.to(), "Zielposition für En-Passant ist falsch.");
    }


    @Test
    public void givenValidEnPassantForBlack_whenGenerateMoves_thenMoveIsGenerated() {
        // Set up move history to indicate a white pawn double move.
        Move<Square8x8, StandardPieceType> doubleAdvancePawnMove = Move.of(
                pawn,
                Square8x8.of(2, 6),
                Square8x8.of(2, 4)
        );

        ChessPiece<StandardPieceType> blackPawn = new ChessPiece<>(StandardPieceType.PAWN, Color.BLACK);
        Square8x8 blackPawnPos = Square8x8.of(3, 4);

        gameState.setPieceAt(blackPawnPos, blackPawn);
        gameState.setPieceAt(doubleAdvancePawnMove.to(), doubleAdvancePawnMove.movedPiece());
        gameState.getMoveHistory().recordMove(doubleAdvancePawnMove);

        // Generate en passant moves.
        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        tested.generateMoves(gameState, moves);

        // Expect one en passant move, with target square (3,6).
        assertEquals(1, moves.size(), "There should be one en passant move for White.");
        Move<Square8x8, StandardPieceType> enPassantMove = Move.of(
                blackPawn,
                blackPawnPos,
                Square8x8.of(2, 5),
                new EnPassantComponent<>(doubleAdvancePawnMove.movedPiece(), Square8x8.of(2, 4))
        );
        assertEquals(enPassantMove, moves.getFirst());
    }


    @Test
    public void givenNoAdjacentPawns_whenGenerateMoves_thenNoEnPassant() {
        Move<Square8x8, StandardPieceType> randomMove = Move.of(
                new ChessPiece<>(StandardPieceType.KING, Color.BLACK),
                Square8x8.of(0, 0),
                Square8x8.of(1, 0)
        );

        gameState.setPieceAt(randomMove.to(), randomMove.movedPiece());
        gameState.getMoveHistory().recordMove(randomMove);

        MoveLookup<Square8x8, StandardPieceType> moves = new MoveLookup<>();
        tested.generateMoves(gameState, moves);

        assertTrue(moves.isEmpty(), "En passant should not be possible if there are no adjacent enemy pawns.");
    }
}
