package de.schoenfeld.chess.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BitMapTest {
    private BitMap bitMapSmall;
    private BitMap bitMapLarge;
    private BitMap bitMapAnother;

    @BeforeEach
    void setUp() {
        bitMapSmall = new BitMap(10);   // Small bitmap with 10 bits
        bitMapLarge = new BitMap(100);  // Larger bitmap with 100 bits
        bitMapAnother = new BitMap(10); // Another bitmap for logical operations
    }

    @Test
    void givenEmptyBitMap_whenSetBits_thenBitsAreSetCorrectly() {
        bitMapSmall.set(3);
        bitMapSmall.set(7);
        assertTrue(bitMapSmall.get(3));
        assertTrue(bitMapSmall.get(7));
        assertFalse(bitMapSmall.get(5));

        bitMapLarge.set(25);
        bitMapLarge.set(99);
        assertTrue(bitMapLarge.get(25));
        assertTrue(bitMapLarge.get(99));
        assertFalse(bitMapLarge.get(50));
    }

    @Test
    void givenSetBits_whenCleared_thenBitsAreUnset() {
        bitMapSmall.set(3);
        bitMapSmall.set(7);
        bitMapSmall.clear(3);
        assertFalse(bitMapSmall.get(3));
        assertTrue(bitMapSmall.get(7));

        bitMapLarge.set(25);
        bitMapLarge.set(99);
        bitMapLarge.clear(99);
        assertFalse(bitMapLarge.get(99));
        assertTrue(bitMapLarge.get(25));
    }

    @Test
    void givenUnsetBit_whenToggled_thenBitIsSet() {
        bitMapSmall.toggle(2);
        assertTrue(bitMapSmall.get(2));

        bitMapLarge.toggle(50);
        assertTrue(bitMapLarge.get(50));
    }

    @Test
    void givenSetBit_whenToggled_thenBitIsUnset() {
        bitMapSmall.set(2);
        bitMapSmall.toggle(2);
        assertFalse(bitMapSmall.get(2));

        bitMapLarge.set(50);
        bitMapLarge.toggle(50);
        assertFalse(bitMapLarge.get(50));
    }

    @Test
    void givenEmptyBitMap_whenFill_thenAllBitsAreSet() {
        bitMapSmall.fill();
        for (int i = 0; i < bitMapSmall.size(); i++) {
            assertTrue(bitMapSmall.get(i));
        }

        bitMapLarge.fill();
        assertEquals(bitMapLarge.size(), bitMapLarge.countBits());
    }

    @Test
    void givenFilledBitMap_whenClearAll_thenAllBitsAreUnset() {
        bitMapSmall.fill();
        bitMapSmall.clearAll();
        for (int i = 0; i < bitMapSmall.size(); i++) {
            assertFalse(bitMapSmall.get(i));
        }

        bitMapLarge.fill();
        bitMapLarge.clearAll();
        assertEquals(0, bitMapLarge.countBits());
    }

    @Test
    void givenBitMapWithSetBits_whenCountBits_thenCorrectCountIsReturned() {
        assertEquals(0, bitMapSmall.countBits());

        bitMapSmall.set(0);
        bitMapSmall.set(9);
        assertEquals(2, bitMapSmall.countBits());

        bitMapLarge.set(10);
        bitMapLarge.set(50);
        bitMapLarge.set(99);
        assertEquals(3, bitMapLarge.countBits());

        bitMapLarge.fill();
        assertEquals(100, bitMapLarge.countBits());
    }

    @Test
    void givenBitMapWithSetBits_whenForEachSetBit_thenIteratesCorrectly() {
        bitMapSmall.set(1);
        bitMapSmall.set(5);
        bitMapSmall.set(8);

        List<Integer> collectedBits = new ArrayList<>();
        bitMapSmall.forEachSetBit(collectedBits::add);
        assertEquals(List.of(1, 5, 8), collectedBits);

        bitMapLarge.set(10);
        bitMapLarge.set(99);
        collectedBits.clear();
        bitMapLarge.forEachSetBit(collectedBits::add);
        assertEquals(List.of(10, 99), collectedBits);
    }

    @Test
    void givenBitMap_whenGetSize_thenCorrectSizeIsReturned() {
        assertEquals(10, bitMapSmall.size());
        assertEquals(100, bitMapLarge.size());
    }

    @Test
    void givenOutOfBoundsIndex_whenAccessed_thenThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> bitMapSmall.set(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> bitMapSmall.set(10));

        assertThrows(IndexOutOfBoundsException.class, () -> bitMapLarge.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> bitMapLarge.get(100));

        assertThrows(IndexOutOfBoundsException.class, () -> bitMapLarge.clear(101));
    }

    @Test
    void givenBitMapWithSetBits_whenToString_thenCorrectBinaryStringIsReturned() {
        bitMapSmall.set(1);
        bitMapSmall.set(5);
        bitMapSmall.set(8);
        assertEquals("0100010010", bitMapSmall.toString());

        bitMapLarge.set(0);
        bitMapLarge.set(99);
        String expectedLarge = "1" + "0".repeat(98) + "1";
        assertEquals(expectedLarge, bitMapLarge.toString());
    }

    // Testing logical operations

    @Test
    void givenTwoBitMaps_whenOr_thenReturnsCorrectBitMap() {
        bitMapSmall.set(1);
        bitMapSmall.set(3);
        bitMapAnother.set(3);
        bitMapAnother.set(4);

        BitMap result = bitMapSmall.or(bitMapAnother);

        assertTrue(result.get(1));
        assertTrue(result.get(3));
        assertTrue(result.get(4));
        assertFalse(result.get(2));
    }

    @Test
    void givenTwoBitMaps_whenAnd_thenReturnsCorrectBitMap() {
        bitMapSmall.set(1);
        bitMapSmall.set(3);
        bitMapAnother.set(3);
        bitMapAnother.set(4);

        BitMap result = bitMapSmall.and(bitMapAnother);

        assertFalse(result.get(1));
        assertTrue(result.get(3));
        assertFalse(result.get(4));
    }

    @Test
    void givenTwoBitMaps_whenNand_thenReturnsCorrectBitMap() {
        bitMapSmall.set(1);
        bitMapSmall.set(3);
        bitMapAnother.set(3);
        bitMapAnother.set(4);

        BitMap result = bitMapSmall.nand(bitMapAnother);

        assertTrue(result.get(1));
        assertFalse(result.get(3));
        assertTrue(result.get(4));
    }

    @Test
    void givenTwoBitMaps_whenXor_thenReturnsCorrectBitMap() {
        bitMapSmall.set(1);
        bitMapSmall.set(3);
        bitMapAnother.set(3);
        bitMapAnother.set(4);

        BitMap result = bitMapSmall.xor(bitMapAnother);

        assertTrue(result.get(1));
        assertFalse(result.get(3));
        assertTrue(result.get(4));
    }

    @Test
    void givenBitMap_whenNot_thenReturnsCorrectBitMap() {
        bitMapSmall.set(1);
        bitMapSmall.set(3);

        BitMap result = bitMapSmall.not();

        assertFalse(result.get(1));
        assertTrue(result.get(2)); // 2 should be set because it wasn't in the original bitmap
        assertFalse(result.get(3));
    }
}
