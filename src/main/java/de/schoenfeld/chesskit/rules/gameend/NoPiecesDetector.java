package de.schoenfeld.chesskit.rules.gameend;

import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.board.tile.Tile;
import de.schoenfeld.chesskit.events.GameConclusion;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.PieceType;
import de.schoenfeld.chesskit.model.StandardPieceType;

public class NoPiecesDetector<T extends Tile, P extends PieceType> implements GameConclusionDetector<T, P> {
    private static final NoPiecesDetector<Square8x8, StandardPieceType> STANDARD = new NoPiecesDetector<>();

    public static NoPiecesDetector<Square8x8, StandardPieceType> standard() {
        return STANDARD;
    }

    @Override
    public GameConclusion detectConclusion(GameState<T, P> gameState) {
        if (gameState.getChessBoard().getTilesWithColour(gameState.getColor()).isEmpty())
            return new GameConclusion(GameConclusion.Winner.of(gameState.getColor().opposite()),
                    "No pieces left for the other player");
        return null;
    }
}
