package de.schoenfeld.chesskit.core.ai;

import de.schoenfeld.chesskit.core.Player;
import de.schoenfeld.chesskit.events.*;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.PlayerData;
import de.schoenfeld.chesskit.move.Move;

public class AIPlayer<T extends PieceType> extends Player<T> {
    private final MoveSearchStrategy<T> searchStrategy;

    public AIPlayer(PlayerData playerData,
                    EventBus eventBus,
                    MoveSearchStrategy<T> searchStrategy) {
        super(playerData, eventBus);
        this.searchStrategy = searchStrategy;
    }

    @Override
    protected void onGameStateChanged(GameStateChangedEvent<T> event) {
        if (shouldAct(event.newState())) {
            processMove(event.newState());
        }
    }

    private boolean shouldAct(GameState<T> state) {
        return state.isWhiteTurn() == playerData.isWhite();
    }

    private void processMove(GameState<T> state) {
        Move<T> move = searchStrategy.searchMove(state);
        eventBus.publish(new MoveProposedEvent<>(
                gameId,
                playerData,
                move
        ));
    }

    @Override
    protected void onGameEnded(GameEndedEvent event) {
        // Optional: Log game conclusion or learn from results
    }

    @Override
    protected void onError(ErrorEvent event) {
        // Optional: Implement error recovery strategies
    }
}