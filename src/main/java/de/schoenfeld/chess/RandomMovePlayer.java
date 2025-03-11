package de.schoenfeld.chess;

import de.schoenfeld.chess.events.*;
import de.schoenfeld.chess.model.PlayerData;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.move.MoveGenerator;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RandomMovePlayer extends Player {
    private final Random random;
    private final MoveGenerator moveGenerator;

    public RandomMovePlayer(PlayerData data, EventBus eventBus) {
        super(data, eventBus);
        random = new Random(System.nanoTime());
        moveGenerator = new MoveGenerator();
    }

    @Override
    protected void onGameEnded(GameEndedEvent event) {

    }

    @Override
    protected void onGameStateChanged(GameStateChangedEvent event) {
        if (event.newState().isWhiteTurn() == playerData.isWhite()) {
            MoveCollection moves = moveGenerator.generateMoves(event.newState());

            if (playerData.isWhite()) try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Throwable ignored) {
            }
            int randomMoveIdx = random.nextInt(moves.size());
            Move randomMove = null;
            int i = 0;
            for (Move move : moves) {
                if (i == randomMoveIdx) randomMove = move;
                i++;
            }

            eventBus.publish(new MoveProposedEvent(gameId, playerData, randomMove));
        }
    }

    @Override
    protected void onError(ErrorEvent event) {
        throw new RuntimeException(event.errorMessage());
    }
}
