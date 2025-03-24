package de.schoenfeld.chess.board;

public class ListChessBoardTest extends ChessBoardTest {

    @Override
    protected void setUpBoard() {
        tested = new ListChessBoard<>(bounds);
    }
}
