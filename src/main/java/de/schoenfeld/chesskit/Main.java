package de.schoenfeld.chesskit;

import de.schoenfeld.chesskit.board.BoardUtility;
import de.schoenfeld.chesskit.core.ChessGame;
import de.schoenfeld.chesskit.core.Player;
import de.schoenfeld.chesskit.core.UIPlayer;
import de.schoenfeld.chesskit.core.ai.AIPlayer;
import de.schoenfeld.chesskit.core.ai.AlphaBetaNegamax;
import de.schoenfeld.chesskit.core.ai.SimpleEvaluator;
import de.schoenfeld.chesskit.core.ai.SimpleMoveOrdering;
import de.schoenfeld.chesskit.events.EventBus;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PlayerData;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.rules.Rules;
import de.schoenfeld.chesskit.ui.ChessUIClient;
import de.schoenfeld.chesskit.ui.DefaultTheme;
import de.schoenfeld.chesskit.ui.PieceRenderer;
import de.schoenfeld.chesskit.ui.Theme;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Rules<StandardPieceType> rules = Rules.standard();
        GameState<StandardPieceType> gameState = new GameState<>(BoardUtility.getDefaultBoard(), rules);

        EventBus eventBus = new EventBus();
        PlayerData black = new PlayerData(UUID.randomUUID(), "Schwarz", false);
        PlayerData white = new PlayerData(UUID.randomUUID(), "Weiß", true);

        AlphaBetaNegamax<StandardPieceType> blackStrategy = AlphaBetaNegamax.<StandardPieceType>builder()
                .evaluator(new SimpleEvaluator(rules))
                .rules(rules)
                .heuristic(new SimpleMoveOrdering())
                .maxDepth(3)
                .build();

        Theme theme = new DefaultTheme();
        ChessUIClient client = new ChessUIClient(eventBus, new PieceRenderer(theme), theme);
        Player<StandardPieceType> whitePlayer = new UIPlayer<>(white, eventBus, client, rules);
        Player<StandardPieceType> blackPlayer = new AIPlayer<>(black, eventBus, blackStrategy);
        ChessGame<StandardPieceType> chessGame = new ChessGame<>(gameState, rules, eventBus);

        client.show();
        chessGame.start();
    }
}
