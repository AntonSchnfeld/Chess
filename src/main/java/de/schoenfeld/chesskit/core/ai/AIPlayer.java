package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.core.Player;
import de.schoenfeld.chesskit.events.*;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.PlayerData;
import de.schoenfeld.chesskit.move.Move;

public class AIPlayer<T extends Tile, P extends PieceType> extends Player<T, P> {
    private final MoveSearchStrategy<T, P> searchStrategy;

    public AIPlayer(PlayerData playerData,
                    EventBus eventBus,
                    MoveSearchStrategy<T, P> searchStrategy) {
        super(playerData, eventBus);
        this.searchStrategy = searchStrategy;
    }

    @Override
    protected void onGameStateChanged(GameStateChangedEvent<T, P> event) {
        if (shouldAct(event.newState())) {
            processMove(event.newState());
        }
    }

    private boolean shouldAct(GameState<T, P> state) {
        return state.getColor() == playerData.color();
    }

    private void processMove(GameState<T, P> state) {
        Move<T, P> move = searchStrategy.searchMove(state);
        eventBus.publish(new MoveProposedEvent<>(
                gameId,
                playerData,
                move
        ));
    }

    @Override
    protected void onGameEnded(GameEndedEvent event) {
        // Optional: log game conclusion or learn from results
    }

    @Override
    protected void onError(ErrorEvent event) {
        // Optional: implement error recovery strategies
    }
}