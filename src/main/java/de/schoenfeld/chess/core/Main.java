package de.schoenfeld.chess.core;

import de.schoenfeld.chess.Player;
import de.schoenfeld.chess.RandomMovePlayer;
import de.schoenfeld.chess.UIClient;
import de.schoenfeld.chess.board.BoardUtility;
import de.schoenfeld.chess.events.EventBus;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.MoveHistory;
import de.schoenfeld.chess.model.PlayerData;
import de.schoenfeld.chess.move.MoveGenerator;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        System.setProperty("java.util.logging.config.file", "src/main/resources/logging.properties");

        EventBus eventBus = new EventBus();

        GameState gameState = new GameState(BoardUtility.getDefaultBoard(), new MoveHistory());
        UIClient client = new UIClient(eventBus);
        Player white = new RandomMovePlayer(new PlayerData(UUID.randomUUID(), "Weiss", true), eventBus);
        Player black = new RandomMovePlayer(new PlayerData(UUID.randomUUID(), "Schwarz", false), eventBus);
        ChessGame chessGame = new ChessGame(gameState, new MoveGenerator(), eventBus);
        client.show();
        chessGame.start();
    }
}
