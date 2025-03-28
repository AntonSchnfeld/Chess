package de.schoenfeld.chesskit.board;

public class MapChessBoardTest extends ChessBoardTest {

    @Override
    protected void setUpBoard() {
        tested = new MapChessBoard<>(bounds);
    }
}
