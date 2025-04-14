package de.schoenfeld.chesskit.board;

import de.schoenfeld.chesskit.board.tile.Square8x8;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public record Square8x8ChessBoardBounds() implements ChessBoardBounds<Square8x8> {
    private static final List<Square8x8> SQUARES = new ArrayList<>();

    static {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                SQUARES.add(Square8x8.of(x, y));
            }
        }
    }

    @Override
    public boolean contains(Square8x8 tile) {
        return tile.x() < 8 && tile.y() < 8 && tile.x() >= 0 && tile.y() >= 0;
    }

    @Override
    public Collection<Square8x8> getTiles() {
        return Collections.unmodifiableList(SQUARES);
    }

    @Override
    public int getTileCount() {
        return 64;
    }
}
