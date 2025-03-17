package de.schoenfeld.chess.rules.generative;

import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PieceType;
import de.schoenfeld.chess.model.Position;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.components.CaptureComponent;

/**
 * A rule that generates en passant moves.
 * En passant is a special pawn capture that can only be done immediately
 * after a pawn moves two squares forward from its starting position,
 * and it could have been captured by an enemy pawn had it moved only one square forward.
 * The opponent captures the just-moved pawn "as it passes" through the first square.
 * The result is the same as if the pawn had moved only one square forward
 * and the enemy pawn had captured it normally.
 * <a href="https://en.wikipedia.org/wiki/En_passant">En passant</a>
 *
 * @author Anton Schoenfeld
 */
public class EnPassantRule implements GenerativeMoveRule {
    /**
     * Generates a collection of en passant moves.
     *
     * @param gameState The current game state
     * @return A {@link MoveCollection} containing the generated moves
     * @throws NullPointerException if {@code gameState} is null
     */
    @Override
    public MoveCollection generateMoves(GameState gameState) {
        var board = gameState.chessBoard();
        var history = gameState.moveHistory();

        // check if there are any moves
        if (history.getMoveCount() == 0) return new MoveCollection();

        // check if the last move was a pawn move
        var lastMove = history.getLastMove();
        if (!PieceType.PAWN.equals(lastMove.movedPiece().pieceType()))
            return new MoveCollection();

        // check if the last move was a double pawn move
        if (Math.abs(lastMove.from().y() - lastMove.to().y()) != 2)
            return new MoveCollection();

        int direction = lastMove.movedPiece().isWhite() ? 1 : -1;
        Position enPassantTarget = Position.of(lastMove.to().x(), lastMove.to().y() + direction);
        MoveCollection moves = new MoveCollection();

        // Find the pawn that can capture the en passant target
        var enPassantPawn = board.getPieceAt(enPassantTarget.offset(1, -direction));
        // Check if the pawn is not null and is of the opposite color
        if (enPassantPawn != null
                && enPassantPawn.isWhite() != lastMove.movedPiece().isWhite()
                && enPassantPawn.pieceType().equals(PieceType.PAWN)) {
            moves.add(Move.of(
                    enPassantPawn,
                    board.getPiecePosition(enPassantPawn),
                    enPassantTarget,
                    new CaptureComponent(enPassantPawn)
            ));
        }

        enPassantPawn = board.getPieceAt(enPassantTarget.offset(-1, -direction));
        if (enPassantPawn != null
                && enPassantPawn.isWhite() != lastMove.movedPiece().isWhite()
                && enPassantPawn.pieceType().equals(PieceType.PAWN)) {
            moves.add(Move.of(
                    enPassantPawn,
                    board.getPiecePosition(enPassantPawn),
                    enPassantTarget,
                    new CaptureComponent(enPassantPawn)
            ));
        }

        return moves;
    }
}
