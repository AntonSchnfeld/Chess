package de.schoenfeld.chess;

import de.schoenfeld.chess.model.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PositionTest {

    private Position startPosition;

    @BeforeEach
    public void setUp() {
        startPosition = new Position(0, 0);
    }

    @Test
    public void givenValidOffsetIntegers_whenOffset_thenReturnExpectedOffsetPosition() {
        Position actualValue = startPosition.offset(4, 4);
        Position expectedValue = new Position(4, 4);
        Assertions.assertEquals(actualValue, expectedValue);
    }

    @Test
    public void givenValidOffsetPosition_whenOffset_thenReturnExpectedOffsetPosition() {
        Position offsetPosition = new Position(4, 4);
        Position actualValue = startPosition.offset(offsetPosition);
        Position expectedValue = new Position(4, 4);
        Assertions.assertEquals(actualValue, expectedValue);
    }

    @Test
    public void givenNullOffsetPosition_whenOffset_thenThrowIllegalArgumentException() {
        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> startPosition.offset(null));
        Assertions.assertEquals("position must not be null", exception.getMessage());
    }
}
