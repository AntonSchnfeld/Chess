package de.schoenfeld.chess.rules.gameend;

import de.schoenfeld.chess.board.ChessBoard;
import de.schoenfeld.chess.events.GameConclusion;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.Square;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.MoveGenerator;

import java.util.List;
import java.util.Optional;

public class CheckMateRule implements GameConclusionRule<StandardPieceType> {
    private final MoveGenerator<StandardPieceType> moveGenerator;

    public CheckMateRule(MoveGenerator<StandardPieceType> moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    @Override
    public Optional<GameConclusion> detectGameEndCause(GameState<StandardPieceType> gameState) {
        // No need for further computation if no king is in check
        if (allKingsSafe(gameState)) return Optional.empty();
        // Detect all possible moves for the checked player
        MoveCollection<StandardPieceType> moves = moveGenerator.generateMoves(gameState);
        // Could perhaps be faster to sort moves according to which ones are most likely to
        // prevent check since checking fewer moves is faster
        for (Move<StandardPieceType> move : moves) {
            // Simulate move
            GameState<StandardPieceType> newState = move.executeOn(gameState);
            // Use withTurnSwitched to reverse turn change in move.executeOn(GameState)
            if (allKingsSafe(newState.withTurnSwitched())) {
                // Found a move that prevents check
                return Optional.empty();
            }
        }
        // No safe moves found :(
        return Optional.of(new GameConclusion(
                gameState.isWhiteTurn() ? GameConclusion.Winner.BLACK : GameConclusion.Winner.WHITE,
                "Checkmate"
        ));
    }

    private boolean allKingsSafe(GameState<StandardPieceType> gameState) {
        ChessBoard<StandardPieceType> board = gameState.chessBoard();
        boolean isWhiteTurn = gameState.isWhiteTurn();
        // Get all kings
        List<ChessPiece<StandardPieceType>> kings = board
                .getPiecesOfTypeAndColour(StandardPieceType.KING, isWhiteTurn);
        if (kings.isEmpty()) return true; // No kings => no check
        // withTurnSwitched to generate moves for the opposite player
        MoveCollection<StandardPieceType> opponentMoves = moveGenerator
                .generateMoves(gameState.withTurnSwitched());

        for (ChessPiece<StandardPieceType> king : kings) {
            // Get king pos and check if opponent has any move to that square
            Square kingPos = board.getPiecePosition(king);
            if (opponentMoves.containsMoveTo(kingPos)) return false;
        }
        return true; // No check found, everyone is happy
    }
}
