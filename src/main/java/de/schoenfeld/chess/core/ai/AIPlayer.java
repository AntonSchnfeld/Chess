package de.schoenfeld.chess.core.ai;

import de.schoenfeld.chess.core.Player;
import de.schoenfeld.chess.events.*;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.PlayerData;
import de.schoenfeld.chess.move.Move;

public class AIPlayer extends Player {
    private final MoveSearchStrategy searchStrategy;

    public AIPlayer(PlayerData playerData,
                    EventBus eventBus,
                    MoveSearchStrategy searchStrategy) {
        super(playerData, eventBus);
        this.searchStrategy = searchStrategy;
    }

    @Override
    protected void onGameStateChanged(GameStateChangedEvent event) {
        if (shouldAct(event.newState())) {
            processMove(event.newState());
        }
    }

    private boolean shouldAct(GameState state) {
        return state.isWhiteTurn() == playerData.isWhite();
    }

    private void processMove(GameState state) {
        Move move = searchStrategy.searchMove(state);
        eventBus.publish(new MoveProposedEvent(
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