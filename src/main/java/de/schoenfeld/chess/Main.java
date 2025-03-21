package de.schoenfeld.chess;

import de.schoenfeld.chess.board.BoardUtility;
import de.schoenfeld.chess.core.ChessGame;
import de.schoenfeld.chess.core.Player;
import de.schoenfeld.chess.core.ai.*;
import de.schoenfeld.chess.events.EventBus;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.MoveHistory;
import de.schoenfeld.chess.model.PlayerData;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.rules.Rules;
import de.schoenfeld.chess.ui.ChessUIClient;
import de.schoenfeld.chess.ui.DefaultTheme;
import de.schoenfeld.chess.ui.PieceRenderer;
import de.schoenfeld.chess.ui.Theme;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        Rules<StandardPieceType> rules = Rules.DEFAULT;
        GameState<StandardPieceType> gameState = new GameState<StandardPieceType>(
                BoardUtility.getDefaultBoard(),
                new MoveHistory(), true);

        GameStateEvaluator advancedEvaluator = new PieceValueEvaluator();
        GameStateEvaluator otherEvaluator = new SimpleEvaluationFunctionWithMobility(rules);
        MoveSearchStrategy whiteStrategy = new AlphaBetaNegamax(3, Runtime.getRuntime().availableProcessors(), rules, advancedEvaluator);
        MoveSearchStrategy blackStrategy = new AlphaBetaNegamax(3, Runtime.getRuntime().availableProcessors(), rules, otherEvaluator);

        EventBus eventBus = new EventBus();
        PlayerData black = new PlayerData(UUID.randomUUID(), "Schwarz", false);
        PlayerData white = new PlayerData(UUID.randomUUID(), "Weiß", true);

        Theme theme = new DefaultTheme();
        ChessUIClient client = new ChessUIClient(eventBus, new PieceRenderer(theme), theme);
        Player whitePlayer = new AIPlayer(white, eventBus, whiteStrategy);
        Player blackPlayer = new AIPlayer(black, eventBus, blackStrategy);
        ChessGame chessGame = new ChessGame(gameState, rules, eventBus);
        client.show();
        chessGame.start();
    }
}
