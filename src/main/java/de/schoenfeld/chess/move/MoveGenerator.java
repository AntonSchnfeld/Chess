package de.schoenfeld.chess.move;

import de.schoenfeld.chess.board.ImmutableChessBoard;
import de.schoenfeld.chess.model.ChessPiece;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.rules.RuleEngine;

import java.util.List;

public class MoveGenerator {
    private final RuleEngine ruleEngine;

    public MoveGenerator(RuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    public MoveGenerator() {
        this(RuleEngine.DEFAULT_ENGINE);
    }

    public MoveCollection generateMoves(GameState gameState) {
        MoveCollection moves = new MoveCollection();

        ImmutableChessBoard chessBoard = gameState.chessBoard();

        List<ChessPiece> pieces = chessBoard.getPiecesOfColour(gameState.isWhiteTurn());

        for (ChessPiece piece : pieces)
            moves.addAll(piece.getPieceType().moveStrategy().getPseudoLegalMoves(chessBoard,
                    chessBoard.getPiecePosition(piece)));

        ruleEngine.applyRules(moves, gameState);

        return moves;
    }
}
