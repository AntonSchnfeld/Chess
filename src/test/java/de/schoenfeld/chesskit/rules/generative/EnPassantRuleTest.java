package de.schoenfeld.chesskit.rules.generative;

import de.schoenfeld.chesskit.model.*;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.move.components.EnPassantComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EnPassantRuleTest {
    private EnPassantRule tested;
    private GameState<StandardPieceType> gameState;
    private ChessPiece<StandardPieceType> pawn;
    private Square pawnPos;

    @BeforeEach
    public void setup() {
        tested = new EnPassantRule();
        gameState = new GameState<>();

        pawn = new ChessPiece<>(StandardPieceType.PAWN, true);
        pawnPos = Square.of(3, 3);

        gameState.setPieceAt(pawnPos, pawn);
    }

    @Test
    public void givenNoMoveHistory_whenGenerateMoves_thenNoEnPassant() {
        MoveLookup<StandardPieceType> moves = tested.generateMoves(gameState);

        assertTrue(moves.isEmpty(), "En passant should not be possible without move history.");
    }

    @Test
    public void givenLastMoveNotPawn_whenGenerateMoves_thenNoEnPassant() {
        Move<StandardPieceType> nonPawnMove = Move.of(
                new ChessPiece<>(StandardPieceType.ROOK, false),
                Square.of(2, 1),
                Square.of(2, 3)
        );

        gameState.setPieceAt(nonPawnMove.to(), nonPawnMove.movedPiece());
        gameState.getMoveHistory().recordMove(nonPawnMove);

        MoveLookup<StandardPieceType> moves = tested.generateMoves(gameState);

        assertTrue(moves.isEmpty(), "En passant should not be possible if the last move was not a pawn move.");
    }

    @Test
    public void givenLastMoveNotDoublePawnMove_whenGenerateMoves_thenNoEnPassant() {
        Move<StandardPieceType> pawnMove = Move.of(
                new ChessPiece<>(StandardPieceType.PAWN, false),
                Square.of(2, 2),
                Square.of(2, 3)
        );

        gameState.setPieceAt(pawnMove.to(), pawnMove.movedPiece());
        gameState.getMoveHistory().recordMove(pawnMove);

        MoveLookup<StandardPieceType> moves = tested.generateMoves(gameState);

        assertTrue(moves.isEmpty(), "En passant should not be possible if the last pawn move was not a double move.");
    }

    @Test
    public void givenValidEnPassantForWhite_whenGenerateMoves_thenMoveIsGenerated() {
        // Weißer Bauer auf der korrekten En-Passant Position nach Gegner-Doppelschritt
        pawnPos = Square.of(4, 4);
        pawn = new ChessPiece<>(StandardPieceType.PAWN, true);

        gameState.setPieceAt(pawnPos, pawn);

        // Gegnerischer (schwarzer) Bauer machte gerade zuvor Doppelschritt von (3,6) zu (3,4)
        Square enemyPawnStart = Square.of(3, 6);
        Square enemyPawnEnd = Square.of(3, 4);
        ChessPiece<StandardPieceType> enemyPawn = new ChessPiece<>(StandardPieceType.PAWN, false);
        gameState.setPieceAt(enemyPawnEnd, enemyPawn);

        Move<StandardPieceType> enemyPawnDoubleMove = Move.of(enemyPawn, enemyPawnStart, enemyPawnEnd);
        enemyPawnDoubleMove.executeOn(gameState); // Historie hinzufügen

        MoveLookup<StandardPieceType> moves = tested.generateMoves(gameState);

        assertEquals(1, moves.size(), "Ein En-Passant-Zug muss generiert werden.");
        Move<StandardPieceType> move = moves.iterator().next();
        assertTrue(move.hasComponent(EnPassantComponent.class), "Zug sollte eine EnPassantComponent haben.");
        assertEquals(Square.of(3, 5), move.to(), "Zielposition für En-Passant ist falsch.");
    }


    @Test
    public void givenValidEnPassantForBlack_whenGenerateMoves_thenMoveIsGenerated() {
        // Set up move history to indicate a white pawn double move.
        Move<StandardPieceType> doubleAdvancePawnMove = Move.of(
                pawn,
                Square.of(2, 6),
                Square.of(2, 4)
        );

        ChessPiece<StandardPieceType> blackPawn = new ChessPiece<>(StandardPieceType.PAWN, false);
        Square blackPawnPos = Square.of(3, 4);

        gameState.setPieceAt(blackPawnPos, blackPawn);
        gameState.setPieceAt(doubleAdvancePawnMove.to(), doubleAdvancePawnMove.movedPiece());
        gameState.getMoveHistory().recordMove(doubleAdvancePawnMove);

        // Generate en passant moves.
        MoveLookup<StandardPieceType> moves = tested.generateMoves(gameState);

        // Expect one en passant move, with target square (3,6).
        assertEquals(1, moves.size(), "There should be one en passant move for White.");
        Move<StandardPieceType> enPassantMove = Move.of(
                blackPawn,
                blackPawnPos,
                Square.of(2, 5),
                new EnPassantComponent(doubleAdvancePawnMove.movedPiece(), Square.of(2, 4))
        );
        assertEquals(enPassantMove, moves.getFirst());
    }


    @Test
    public void givenNoAdjacentPawns_whenGenerateMoves_thenNoEnPassant() {
        Move<StandardPieceType> randomMove = Move.of(
                new ChessPiece<>(StandardPieceType.KING, false),
                Square.of(0, 0),
                Square.of(1, 0)
        );

        gameState.setPieceAt(randomMove.to(), randomMove.movedPiece());
        gameState.getMoveHistory().recordMove(randomMove);

        MoveLookup<StandardPieceType> moves = tested.generateMoves(gameState);

        assertTrue(moves.isEmpty(), "En passant should not be possible if there are no adjacent enemy pawns.");
    }
}
