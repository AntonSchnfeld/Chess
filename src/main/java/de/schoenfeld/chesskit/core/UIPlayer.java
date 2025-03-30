package de.schoenfeld.chesskit.core;

import de.schoenfeld.chesskit.events.*;
import de.schoenfeld.chesskit.model.*;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.move.MoveLookup;
import de.schoenfeld.chesskit.rules.Rules;
import de.schoenfeld.chesskit.ui.ChessBoardPanel;
import de.schoenfeld.chesskit.ui.ChessSquare;
import de.schoenfeld.chesskit.ui.ChessUIClient;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class UIPlayer<T extends PieceType> extends Player<T> {
    private final ChessUIClient uiClient;
    private final Rules<T> rules;
    private Square selectedSquare;
    private MoveLookup<T> legalMoves;
    private GameState<T> gameState;

    public UIPlayer(PlayerData playerData, EventBus eventBus, ChessUIClient uiClient, Rules<T> rules) {
        super(playerData, eventBus);
        this.uiClient = uiClient;
        this.selectedSquare = null;
        this.legalMoves = MoveLookup.of();
        this.rules = rules;

        registerClickListener();
    }

    private void registerClickListener() {
        ChessBoardPanel panel = uiClient.getBoardPanel();
        Map<Square, ChessSquare> squares = panel.getSquares();

        for (Map.Entry<Square, ChessSquare> entry : squares.entrySet()) {
            entry.getValue().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mousePressed(e);
                    if (gameState.isWhiteTurn() == playerData.isWhite())
                        handleBoardClick(entry.getKey());
                }
            });
        }
    }

    private void handleBoardClick(Square clickedSquare) {
        ChessPiece<T> piece = gameState.getPieceAt(clickedSquare);

        // No piece selected yet
        if (selectedSquare == null) {
            // Select our piece and highlight its moves
            if (piece != null && piece.isWhite() == playerData.isWhite()) {
                selectedSquare = clickedSquare;
                MoveLookup<T> movesForPiece = legalMoves.getMovesFromSquare(clickedSquare);
                for (Move<T> move : movesForPiece)
                    uiClient.getBoardPanel().setSquareHighlight(move.to(), true);
            } else {
                // Enemy piece or no piece
                clearSelection();
            }
            return;
        }

        MoveLookup<T> movesForSelectedPiece = legalMoves.getMovesFromSquare(selectedSquare);
        // Publish move if legal
        if (movesForSelectedPiece.containsMoveTo(clickedSquare)) {
            Move<T> theMove = movesForSelectedPiece.getMovesTo(clickedSquare).getFirst();
            MoveProposedEvent<T> event = new MoveProposedEvent<>(gameId, playerData, theMove);
            eventBus.publish(event);
        } else clearSelection();
    }

    @Override
    protected void onGameEnded(GameEndedEvent event) {
        clearSelection();
    }

    @Override
    protected void onGameStateChanged(GameStateChangedEvent<T> event) {
        gameState = event.newState();
        if (event.newState().isWhiteTurn() == playerData.isWhite()) legalMoves = rules.generateMoves(event.newState());
        clearSelection();
    }

    @Override
    protected void onError(ErrorEvent event) {
        if (event.player().equals(playerData))
            JOptionPane.showMessageDialog(uiClient.getBoardPanel(),
                    "Error: " + event.errorMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void clearSelection() {
        selectedSquare = null;
        uiClient.getBoardPanel().clearHighlights();
    }
}
