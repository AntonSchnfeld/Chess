package de.schoenfeld.chess;

import de.schoenfeld.chess.board.BoardUtility;
import de.schoenfeld.chess.core.ChessGame;
import de.schoenfeld.chess.core.Player;
import de.schoenfeld.chess.events.EventBus;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.MoveHistory;
import de.schoenfeld.chess.model.PlayerData;
import de.schoenfeld.chess.rules.Rules;
import de.schoenfeld.chess.rules.generative.CastlingRule;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.*;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    static {
        try {

            try (InputStream is = Main.class.getResourceAsStream("/logging.properties")) {
                LogManager.getLogManager().readConfiguration(is);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to load logging configuration", e);
            }

            // Format: log_YYYY-MM-DD_HH-MM-SS.log
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String logFileName = "log_" + timestamp + ".log";

            FileHandler fileHandler = new FileHandler(logFileName, true); // Append mode
            fileHandler.setLevel(Level.FINEST);
            fileHandler.setFormatter(new SimpleFormatter());

            Logger rootLogger = LogManager.getLogManager().getLogger("");
            rootLogger.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to set up file logging", e);
        }
    }

    public static void main(String[] args) {
        EventBus eventBus = new EventBus();

        Rules rules = Rules.DEFAULT;

        GameState gameState = new GameState(BoardUtility.getDefaultBoard(),
                new MoveHistory(), false);
        UIClient client = new UIClient(eventBus);
        Player white = new RandomMovePlayer(
                new PlayerData(UUID.randomUUID(), "Weiss", true),
                eventBus, rules
        );
        Player black = new RandomMovePlayer(
                new PlayerData(UUID.randomUUID(), "Schwarz", false),
                eventBus, rules
        );
        ChessGame chessGame = new ChessGame(gameState, rules, eventBus);
        client.show();
        chessGame.start();
    }
}
