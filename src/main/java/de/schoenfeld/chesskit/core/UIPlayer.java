package de.schoenfeld.chesskit.core;

import de.schoenfeld.chesskit.board.tile.Square8x8;
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

public class UIPlayer extends Player<Square8x8, StandardPieceType> {
    private final ChessUIClient uiClient;
    private final Rules<Square8x8, StandardPieceType> rules;
    private Square8x8 selectedSquare8x8;
    private MoveLookup<Square8x8, StandardPieceType> legalMoves;
    private GameState<Square8x8, StandardPieceType> gameState;

    public UIPlayer(PlayerData playerData,
                    EventBus eventBus,
                    ChessUIClient uiClient,
                    Rules<Square8x8, StandardPieceType> rules) {
        super(playerData, eventBus);
        this.uiClient = uiClient;
        this.selectedSquare8x8 = null;
        this.legalMoves = MoveLookup.of();
        this.rules = rules;

        registerClickListener();
    }

    private void registerClickListener() {
        ChessBoardPanel panel = uiClient.getBoardPanel();
        Map<Square8x8, ChessSquare> squares = panel.getSquares();

        for (Map.Entry<Square8x8, ChessSquare> entry : squares.entrySet()) {
            entry.getValue().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mousePressed(e);
                    if (gameState.getColor() == playerData.color())
                        handleBoardClick(entry.getKey());
                }
            });
        }
    }

    private void handleBoardClick(Square8x8 clickedSquare8x8) {
        ChessPiece<StandardPieceType> piece = gameState.getPieceAt(clickedSquare8x8);

        // No piece selected yet
        if (selectedSquare8x8 == null) {
            // Select the piece and highlight its moves
            if (piece != null && piece.color() == playerData.color()) {
                selectedSquare8x8 = clickedSquare8x8;
                MoveLookup<Square8x8, StandardPieceType> movesForPiece = legalMoves.getMovesFromSquare(clickedSquare8x8);
                for (Move<Square8x8, StandardPieceType> move : movesForPiece)
                    uiClient.getBoardPanel().setSquareHighlight(move.to(), true);
            } else {
                // Enemy piece or no piece
                clearSelection();
            }
            return;
        }

        MoveLookup<Square8x8, StandardPieceType> movesForSelectedPiece = legalMoves.getMovesFromSquare(selectedSquare8x8);
        // Publish move if legal
        if (movesForSelectedPiece.containsMoveTo(clickedSquare8x8)) {
            Move<Square8x8, StandardPieceType> theMove = movesForSelectedPiece.getMovesTo(clickedSquare8x8).getFirst();
            MoveProposedEvent<Square8x8, StandardPieceType> event = new MoveProposedEvent<>(gameId, playerData, theMove);
            eventBus.publish(event);
        } else clearSelection();
    }

    @Override
    protected void onGameEnded(GameEndedEvent event) {
        clearSelection();
    }

    @Override
    protected void onGameStateChanged(GameStateChangedEvent<Square8x8, StandardPieceType> event) {
        gameState = event.newState();
        if (event.newState().getColor() == playerData.color()) legalMoves = rules.generateMoves(event.newState());
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
        selectedSquare8x8 = null;
        uiClient.getBoardPanel().clearHighlights();
    }
}
