package de.schoenfeld.chesskit;

import de.schoenfeld.chesskit.board.BoardUtility;
import de.schoenfeld.chesskit.core.ChessGame;
import de.schoenfeld.chesskit.core.Player;
import de.schoenfeld.chesskit.core.UIPlayer;
import de.schoenfeld.chesskit.core.ai.*;
import de.schoenfeld.chesskit.events.EventBus;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.MoveHistory;
import de.schoenfeld.chesskit.model.PlayerData;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.rules.MoveGenerator;
import de.schoenfeld.chesskit.rules.RestrictiveMoveGenerator;
import de.schoenfeld.chesskit.rules.Rules;
import de.schoenfeld.chesskit.rules.SimpleMoveGenerator;
import de.schoenfeld.chesskit.rules.gameend.*;
import de.schoenfeld.chesskit.rules.generative.GenerativeMoveRule;
import de.schoenfeld.chesskit.rules.generative.sliding.QueenMoveRule;
import de.schoenfeld.chesskit.rules.restrictive.CheckRule;
import de.schoenfeld.chesskit.rules.restrictive.FriendlyFireRule;
import de.schoenfeld.chesskit.rules.restrictive.RestrictiveMoveRule;
import de.schoenfeld.chesskit.ui.ChessUIClient;
import de.schoenfeld.chesskit.ui.DefaultTheme;
import de.schoenfeld.chesskit.ui.PieceRenderer;
import de.schoenfeld.chesskit.ui.Theme;

import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        List<GenerativeMoveRule<StandardPieceType>> generativeRules = List.of(QueenMoveRule.standard());
        MoveGenerator<StandardPieceType> simpleGenerator = new SimpleMoveGenerator<>(generativeRules);
        List<RestrictiveMoveRule<StandardPieceType>> restrictiveRules = List.of(new FriendlyFireRule<>(), new CheckRule(simpleGenerator));
        MoveGenerator<StandardPieceType> restrictiveGenerator = new RestrictiveMoveGenerator<>(generativeRules, restrictiveRules);
        List<GameConclusionRule<StandardPieceType>> gameConclusionRules = List.of(new CheckMateRule(restrictiveGenerator), new StaleMateRule(restrictiveGenerator), new InsufficientMaterialRule(), new NoPiecesRule<>());
        Rules<StandardPieceType> rules = Rules.standard();
        GameState<StandardPieceType> gameState = new GameState<>(
                BoardUtility.getDefaultBoard(),
                new MoveHistory<>(),
                true);

        System.out.println(gameState.toFen());

        MoveOrderingHeuristic<StandardPieceType> heuristic = new AggressiveMoveOrdering();
        GameStateEvaluator<StandardPieceType> advancedEvaluator = new AdvancedEvaluator(rules);
        GameStateEvaluator<StandardPieceType> otherEvaluator =
                new SimpleEvaluationFunctionWithMobility(rules);
        MoveSearchStrategy<StandardPieceType> whiteStrategy = AlphaBetaNegamax.<StandardPieceType>builder()
                .maxDepth(3)
                .rules(rules)
                .evaluator(otherEvaluator)
                .heuristic(heuristic)
                .build();
        MoveSearchStrategy<StandardPieceType> blackStrategy = AlphaBetaNegamax.<StandardPieceType>builder()
                .maxDepth(3)
                .rules(rules)
                .evaluator(otherEvaluator)
                .heuristic(heuristic)
                .build();

        EventBus eventBus = new EventBus();
        PlayerData black = new PlayerData(UUID.randomUUID(), "Schwarz", false);
        PlayerData white = new PlayerData(UUID.randomUUID(), "Weiß", true);

        Theme theme = new DefaultTheme();
        ChessUIClient client = new ChessUIClient(eventBus, new PieceRenderer(theme), theme);
        Player<StandardPieceType> whitePlayer = new UIPlayer<>(white, eventBus, client, rules);
        Player<StandardPieceType> blackPlayer = new AIPlayer<>(black, eventBus, whiteStrategy);
        ChessGame<StandardPieceType> chessGame = new ChessGame<>(gameState, rules, eventBus);
        client.show();
        chessGame.start();
    }
}
