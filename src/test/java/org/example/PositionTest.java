package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PositionTest {
    @Test
    public void givenValidOffsetIntegers_whenOffset_thenReturnExpectedOffsetPosition() {
        Position startPosition = new Position(0, 0);
        Position actualValue = startPosition.offset(4, 4);
        Position expectedValue = new Position(4, 4);
        Assertions.assertEquals(actualValue, expectedValue);
    }

    @Test
    public void givenValidOffsetPosition_whenOffset_thenReturnExpectedOffsetPosition() {
        Position startPosition = new Position(0, 0);
        Position offsetPosition = new Position(4, 4);
        Position actualValue = startPosition.offset(offsetPosition);
        Position expectedValue = new Position(4, 4);
        Assertions.assertEquals(actualValue, expectedValue);
    }

    @Test
    public void givenNullOffsetPosition_whenOffset_thenThrowIllegalArgumentException() {
        Position startPosition = new Position(0, 0);
        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> startPosition.offset(null));
        Assertions.assertEquals("position must not be null", exception.getMessage());
    }
}
