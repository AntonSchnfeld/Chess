package de.schoenfeld.chesskit.model;

import de.schoenfeld.chesskit.board.tile.Square8x8;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SquareTest {

    private Square8x8 startSquare8x8;

    @BeforeEach
    public void setUp() {
        startSquare8x8 = Square8x8.of(0, 0);
    }

    @Test
    public void givenValidOffsetIntegers_whenOffset_thenReturnExpectedOffsetPosition() {
        Square8x8 actualValue = startSquare8x8.offset(4, 4);
        Square8x8 expectedValue = Square8x8.of(4, 4);
        Assertions.assertEquals(actualValue, expectedValue);
    }

    @Test
    public void givenValidOffsetPosition_whenOffset_thenReturnExpectedOffsetPosition() {
        Square8x8 offsetSquare8x8 = Square8x8.of(4, 4);
        Square8x8 actualValue = startSquare8x8.offset(offsetSquare8x8);
        Square8x8 expectedValue = Square8x8.of(4, 4);
        Assertions.assertEquals(actualValue, expectedValue);
    }

    @Test
    public void givenNullOffsetPosition_whenOffset_thenThrowIllegalArgumentException() {
        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> startSquare8x8.offset(null));
        Assertions.assertEquals("position must not be null", exception.getMessage());
    }
}
