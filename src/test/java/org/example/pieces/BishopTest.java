package org.example.pieces;

import org.example.ChessBoardBounds;
import org.example.ChessBoardView;
import org.example.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class BishopTest {
    @Test
    public void givenEmptyChessBoardView_whenGetValidMoves_thenReturnExpectedMoves() {
        ChessBoardView mockEmptyView = mock(ChessBoardView.class);
        ChessBoardBounds bounds = new ChessBoardBounds(8, 8);
        Position bishopPosition = new Position(3, 3);
        Bishop bishop = new Bishop(true);

        when(mockEmptyView.getChessBoardBounds()).thenReturn(bounds);
        when(mockEmptyView.getPieceAt(any())).thenReturn(null);
        when(mockEmptyView.getPieceAt(bishopPosition)).thenReturn(bishop);

        List<Position> moves = bishop.getValidMoves(mockEmptyView, bishopPosition);

        for (int i = 0; i < 8; i++) {
            if (i != 3)
                Assertions.assertTrue(moves.contains(new Position(i, i)));
        }

        for (int i = 0, j = 7; i < 8; i++, j++) {
            if (i != 3)
                Assertions.assertTrue(moves.contains(new Position(i, j)));
        }

    }
}
