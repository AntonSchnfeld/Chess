package de.schoenfeld.chess;

import de.schoenfeld.chess.board.BoardUtility;
import de.schoenfeld.chess.core.ChessGame;
import de.schoenfeld.chess.core.Player;
import de.schoenfeld.chess.core.UIPlayer;
import de.schoenfeld.chess.core.ai.*;
import de.schoenfeld.chess.events.EventBus;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.MoveHistory;
import de.schoenfeld.chess.model.PlayerData;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.rules.MoveGenerator;
import de.schoenfeld.chess.rules.RestrictiveMoveGenerator;
import de.schoenfeld.chess.rules.Rules;
import de.schoenfeld.chess.rules.SimpleMoveGenerator;
import de.schoenfeld.chess.rules.gameend.*;
import de.schoenfeld.chess.rules.generative.GenerativeMoveRule;
import de.schoenfeld.chess.rules.generative.sliding.QueenMoveRule;
import de.schoenfeld.chess.rules.restrictive.CheckRule;
import de.schoenfeld.chess.rules.restrictive.FriendlyFireRule;
import de.schoenfeld.chess.rules.restrictive.RestrictiveMoveRule;
import de.schoenfeld.chess.ui.ChessUIClient;
import de.schoenfeld.chess.ui.DefaultTheme;
import de.schoenfeld.chess.ui.PieceRenderer;
import de.schoenfeld.chess.ui.Theme;

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
