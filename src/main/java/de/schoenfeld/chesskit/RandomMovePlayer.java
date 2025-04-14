package de.schoenfeld.chesskit;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.core.Player;
import de.schoenfeld.chesskit.events.*;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.PlayerData;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.MoveGenerator;

import java.util.Random;

public class RandomMovePlayer<T extends Tile, P extends PieceType> extends Player<T, P> {

    private final Random random;
    private final MoveGenerator<T, P> rules;

    public RandomMovePlayer(PlayerData data, EventBus eventBus, MoveGenerator<T, P> rules) {
        super(data, eventBus);
        random = new Random(System.nanoTime());
        this.rules = rules;
    }

    @Override
    protected void onGameEnded(GameEndedEvent event) {

    }

    @Override
    protected void onGameStateChanged(GameStateChangedEvent<T, P> event) {
        if (event.newState().getColor() == playerData.color()) {
            MoveLookup<T, P> moves = rules.generateMoves(event.newState());

            int randomMoveIdx = random.nextInt(moves.size());
            Move<T, P> randomMove = null;
            int i = 0;
            for (Move<T, P> move : moves) {
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
