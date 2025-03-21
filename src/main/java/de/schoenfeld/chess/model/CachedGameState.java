package de.schoenfeld.chess.model;

import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.MoveGenerator;

public class CachedGameState {
    private GameState gameState;
    private MoveCollection validMoves;
    private MoveGenerator moveGenerator;

    public CachedGameState(GameState gameState, MoveCollection validMoves, MoveGenerator moveGenerator) {
        this.gameState = gameState;
        this.validMoves = validMoves;
        this.moveGenerator = moveGenerator;
    }

    public CachedGameState(GameState gameState, MoveGenerator moveGenerator) {
        this.gameState = gameState;
        this.moveGenerator = moveGenerator;
        this.validMoves = moveGenerator.generateMoves(gameState);
    }

    public CachedGameState(MoveGenerator moveGenerator) {
        this.gameState = GameState.createInitial();

    }
}
