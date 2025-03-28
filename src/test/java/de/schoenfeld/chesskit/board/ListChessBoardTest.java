package de.schoenfeld.chesskit.board;

public class ListChessBoardTest extends ChessBoardTest {

    @Override
    protected void setUpBoard() {
        tested = new ListChessBoard<>(bounds);
    }
}
