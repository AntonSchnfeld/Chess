package de.schoenfeld.chess.board;

public class MapChessBoardTest extends ChessBoardTest {

    @Override
    protected void setUpBoard() {
        tested = new MapChessBoard<>(bounds);
    }
}
