package de.schoenfeld.chess.logic;

import de.schoenfeld.chess.data.ReadOnlyChessBoard;
import de.schoenfeld.chess.data.ReadOnlyGameState;
import de.schoenfeld.chess.data.move.Move;
import de.schoenfeld.chess.data.move.MoveCollection;
import de.schoenfeld.chess.logic.piece.ChessPiece;
import de.schoenfeld.chess.logic.rule.RuleEngine;

import java.util.List;

public class MoveGenerator {
    private final RuleEngine ruleEngine;
    private final CheckDetector checkDetector;

    public MoveGenerator(RuleEngine ruleEngine, CheckDetector checkDetector) {
        this.ruleEngine = ruleEngine;
        this.checkDetector = checkDetector;
    }

    public MoveGenerator() {
        this(RuleEngine.DEFAULT_ENGINE, new CheckDetector());
    }

    public MoveCollection generateMoves(ReadOnlyGameState gameState, boolean isWhite) {
        MoveCollection moves = new MoveCollection();

        ReadOnlyChessBoard chessBoard = gameState.getChessBoard();

        List<ChessPiece> pieces = chessBoard.getPieces(isWhite);

        for (ChessPiece piece : pieces)
            moves.addAll(piece.getPieceType().moveStrategy().getPseudoLegalMoves(chessBoard,
                    chessBoard.getPiecePosition(piece)));

        ruleEngine.applyRules(moves, gameState);

        for (Move move : moves)
            if (checkDetector.isCheck(gameState, move)) {
                // TODO: Add check to the move
                moves.remove(move);
            }

        return moves;
    }
}
