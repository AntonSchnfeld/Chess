package de.schoenfeld.chess.logic.componenthandler;

import de.schoenfeld.chess.data.ChessBoard;
import de.schoenfeld.chess.data.GameState;
import de.schoenfeld.chess.data.move.CastlingComponent;
import de.schoenfeld.chess.data.move.Move;

public class CastlingComponentHandler implements MoveComponentHandler<CastlingComponent> {
    @Override
    public void handle(GameState gameState, CastlingComponent component, Move move) {
        Move rookMove = component.rookMove();
        ChessBoard chessBoard = gameState.getChessBoard();
        chessBoard.removePieceAt(rookMove.from());
        chessBoard.setPiece(rookMove.movedPiece(), rookMove.to());
    }
}
