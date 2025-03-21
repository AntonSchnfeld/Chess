package de.schoenfeld.chess.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SquareTest {

    private Square startSquare;

    @BeforeEach
    public void setUp() {
        startSquare = Square.of(0, 0);
    }

    @Test
    public void givenValidOffsetIntegers_whenOffset_thenReturnExpectedOffsetPosition() {
        Square actualValue = startSquare.offset(4, 4);
        Square expectedValue = Square.of(4, 4);
        Assertions.assertEquals(actualValue, expectedValue);
    }

    @Test
    public void givenValidOffsetPosition_whenOffset_thenReturnExpectedOffsetPosition() {
        Square offsetSquare = Square.of(4, 4);
        Square actualValue = startSquare.offset(offsetSquare);
        Square expectedValue = Square.of(4, 4);
        Assertions.assertEquals(actualValue, expectedValue);
    }

    @Test
    public void givenNullOffsetPosition_whenOffset_thenThrowIllegalArgumentException() {
        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> startSquare.offset(null));
        Assertions.assertEquals("position must not be null", exception.getMessage());
    }
}
