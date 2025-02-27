package de.schoenfeld.chess.logic;

import de.schoenfeld.chess.data.GameState;

public class ChessGame {
    private GameState gameState;
    private MoveGenerator moveGenerator;
    private MoveExecutor moveExecutor;

    public ChessGame(GameState gameState, MoveGenerator moveGenerator, MoveExecutor moveExecutor) {
        this.gameState = gameState;
        this.moveGenerator = moveGenerator;
        this.moveExecutor = moveExecutor;
    }

    public ChessGame() {
        this(new GameState(), new MoveGenerator(), new MoveExecutor());
    }

    public GameState getGameState() {
        return gameState;
    }

    public MoveGenerator getMoveGenerator() {
        return moveGenerator;
    }


}
