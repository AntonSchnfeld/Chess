package de.schoenfeld.chesskit;

import de.schoenfeld.chesskit.core.Player;
import de.schoenfeld.chesskit.events.*;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.PlayerData;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveCollection;
import de.schoenfeld.chesskit.rules.MoveGenerator;

import java.util.Random;

public class RandomMovePlayer<T extends PieceType> extends Player<T> {

    private final Random random;
    private final MoveGenerator<T> rules;

    public RandomMovePlayer(PlayerData data, EventBus eventBus, MoveGenerator<T> rules) {
        super(data, eventBus);
        random = new Random(System.nanoTime());
        this.rules = rules;
    }

    @Override
    protected void onGameEnded(GameEndedEvent event) {

    }

    @Override
    protected void onGameStateChanged(GameStateChangedEvent<T> event) {
        if (event.newState().isWhiteTurn() == playerData.isWhite()) {
            MoveCollection<T> moves = rules.generateMoves(event.newState());

            int randomMoveIdx = random.nextInt(moves.size());
            Move<T> randomMove = null;
            int i = 0;
            for (Move<T> move : moves) {
                if (i == randomMoveIdx) randomMove = move;
                i++;
            }
            eventBus.publish(new MoveProposedEvent<>(gameId, playerData, randomMove));
        }
    }

    @Override
    protected void onError(ErrorEvent event) {
        if (event.player().equals(playerData))
            throw new RuntimeException(event.errorMessage());
    }
}
