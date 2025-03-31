package de.schoenfeld.chesskit.model;

import java.util.Arrays;
import java.util.function.IntConsumer;

public final class BitMap {
    private final long[] bits;
    private final int size;

    public BitMap(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive.");
        }
        this.size = size;
        this.bits = new long[(size + 63) / 64]; // Allocate enough longs
    }

    private int wordIndex(int bitIndex) {
        return bitIndex >>> 6; // Equivalent to bitIndex / 64
    }

    private long bitMask(int bitIndex) {
        return 1L << (bitIndex & 63); // Equivalent to bitIndex % 64
    }

    public void set(int index) {
        checkBounds(index);
        bits[wordIndex(index)] |= bitMask(index);
    }

    public void clear(int index) {
        checkBounds(index);
        bits[wordIndex(index)] &= ~bitMask(index);
    }

    public boolean get(int index) {
        checkBounds(index);
        return (bits[wordIndex(index)] & bitMask(index)) != 0;
    }

    public void toggle(int index) {
        checkBounds(index);
        bits[wordIndex(index)] ^= bitMask(index);
    }

    public void fill() {
        Arrays.fill(bits, -1L);
        clearTrailingBits(); // Ensure bits outside range are cleared
    }

    public void clearAll() {
        Arrays.fill(bits, 0);
    }

    public int countBits() {
        int count = 0;
        for (long word : bits) {
            count += Long.bitCount(word);
        }
        return count;
    }

    public void forEachSetBit(IntConsumer action) {
        for (int i = 0; i < bits.length; i++) {
            long word = bits[i];
            while (word != 0) {
                int bitIndex = Long.numberOfTrailingZeros(word);
                action.accept((i * 64) + bitIndex);
                word &= word - 1; // Clear the lowest set bit
            }
        }
    }

    public int size() {
        return size;
    }

    public BitMap or(BitMap bitMap) {
        checkCompatible(bitMap);
        BitMap result = new BitMap(size);
        for (int i = 0; i < bits.length; i++) {
            result.bits[i] = this.bits[i] | bitMap.bits[i];
        }
        return result;
    }

    public BitMap and(BitMap bitMap) {
        checkCompatible(bitMap);
        BitMap result = new BitMap(size);
        for (int i = 0; i < bits.length; i++) {
            result.bits[i] = this.bits[i] & bitMap.bits[i];
        }
        return result;
    }

    public BitMap nand(BitMap bitMap) {
        checkCompatible(bitMap);
        BitMap result = new BitMap(size);
        for (int i = 0; i < bits.length; i++) {
            result.bits[i] = ~(this.bits[i] & bitMap.bits[i]);
        }
        return result;
    }

    public BitMap xor(BitMap bitMap) {
        checkCompatible(bitMap);
        BitMap result = new BitMap(size);
        for (int i = 0; i < bits.length; i++) {
            result.bits[i] = this.bits[i] ^ bitMap.bits[i];
        }
        return result;
    }

    public BitMap not() {
        BitMap result = new BitMap(size);
        for (int i = 0; i < bits.length; i++) {
            result.bits[i] = ~this.bits[i];
        }
        result.clearTrailingBits(); // Ensure bits outside range are cleared
        return result;
    }

    private void checkBounds(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Bit index out claim range: " + index);
        }
    }

    private void checkCompatible(BitMap bitMap) {
        if (bitMap.size != this.size) {
            throw new IllegalArgumentException("Bitmaps must have the same size.");
        }
    }

    private void clearTrailingBits() {
        int extraBits = size & 63; // size % 64
        if (extraBits != 0) {
            bits[bits.length - 1] &= (1L << extraBits) - 1;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(get(i) ? '1' : '0');
        }
        return sb.toString();
    }
}
