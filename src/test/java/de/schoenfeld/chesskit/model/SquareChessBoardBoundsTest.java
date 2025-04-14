package de.schoenfeld.chesskit.model;

import de.schoenfeld.chesskit.board.SquareChessBoardBounds;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SquareChessBoardBoundsTest {
    private SquareChessBoardBounds dimensions;

    @BeforeEach
    public void setUp() {
        dimensions = new SquareChessBoardBounds(8, 8);
    }

    @Test
    public void givenEightByEightDimensionsAndOutOfBoundsPositivePosition_whenContains_thenReturnFalse() {
        Square8x8 outOfBoundsSquare8x8 = Square8x8.of(8, 8);
        Assertions.assertFalse(dimensions.contains(outOfBoundsSquare8x8));
    }

    @Test
    public void givenEightByEightDimensionsAndInBoundsPositivePosition_whenContains_thenReturnTrue() {
        Square8x8 inBoundsSquare8x8 = Square8x8.of(3, 3);
        Assertions.assertTrue(dimensions.contains(inBoundsSquare8x8));
    }

    @Test
    public void givenNullPosition_whenContains_thenReturnFalse() {
        Assertions.assertFalse(dimensions.contains(null));
    }

    @Test
    public void givenNegativeColumns_whenConstructor_thenThrowsIllegalArgumentException() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new SquareChessBoardBounds(8, -1));
        Assertions.assertEquals("columns must be at least 1", exception.getMessage());
    }

    @Test
    public void givenNegativeRows_whenConstructor_thenThrowsIllegalArgumentException() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new SquareChessBoardBounds(-1, 8));
        Assertions.assertEquals("rows must be at least 1", exception.getMessage());
    }

    @Test
    public void givenZeroRows_whenConstructor_thenThrowsIllegalArgumentException() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new SquareChessBoardBounds(0, 8));
        Assertions.assertEquals("rows must be at least 1", exception.getMessage());
    }

    @Test
    public void givenZeroColumns_whenConstructor_thenThrowsIllegalArgumentException() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new SquareChessBoardBounds(8, 0));
        Assertions.assertEquals("columns must be at least 1", exception.getMessage());
    }

    @Test
    public void givenEightByEightDimensionsAndPositionAtUpperBounds_whenContains_thenReturnTrue() {
        Square8x8 upperBoundSquare8x8 = Square8x8.of(7, 7);
        Assertions.assertTrue(dimensions.contains(upperBoundSquare8x8));
    }

    @Test
    public void givenEightByEightDimensionsAndPositionJustOutside_whenContains_thenReturnFalse() {
        Square8x8 outOfBoundsSquare8x8 = Square8x8.of(8, 7);
        Assertions.assertFalse(dimensions.contains(outOfBoundsSquare8x8));
    }
}
