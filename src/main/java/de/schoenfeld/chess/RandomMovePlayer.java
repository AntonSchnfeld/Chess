package de.schoenfeld.chess;

import de.schoenfeld.chess.core.Player;
import de.schoenfeld.chess.events.*;
import de.schoenfeld.chess.model.PlayerData;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.move.MoveCollection;
import de.schoenfeld.chess.rules.Rules;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RandomMovePlayer extends Player {

    private final Random random;
    private final Rules rules;

    public RandomMovePlayer(PlayerData data, EventBus eventBus, Rules rules) {
        super(data, eventBus);
        random = new Random(System.nanoTime());
        this.rules = rules;
    }

    @Override
    protected void onGameEnded(GameEndedEvent event) {

    }

    @Override
    protected void onGameStateChanged(GameStateChangedEvent event) {
        if (event.newState().isWhiteTurn() == playerData.isWhite()) {

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            MoveCollection moves = rules.generateMoves(event.newState());

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
        if (event.player().equals(playerData))
            throw new RuntimeException(event.errorMessage());
    }
}
