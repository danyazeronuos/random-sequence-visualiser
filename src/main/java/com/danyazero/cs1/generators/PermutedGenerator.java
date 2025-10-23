package com.danyazero.cs1.generators;

import com.danyazero.cs1.model.BatchGenerator;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class PermutedGenerator extends BatchGenerator {
    private long state;

    public PermutedGenerator(long seed, int batchSize, BiConsumer<long[], BatchGenerator> batchCallback) {
        super(batchSize, batchCallback);
        this.state = 0;
        nextLong();
        this.state += seed;
        nextLong();
    }

    public PermutedGenerator(long seed) {
        this(seed, 0, null);
    }

    @Override
    public long[] generate(int size) {
        var result = new long[size];
        for (int i = 0; i < size; i++) {
            result[i] = nextLong();
            if (this.batchSize > 0 && i % this.batchSize == 0) {
                long[] batch = Arrays.copyOfRange(result, 0, i);
                this.batchCallback.accept(batch, this);
            }

        }

        return result;
    }

    public long nextLong() {
        state += 0x9e3779b97f4a7c15L;
        long x = state;
        x ^= (x >>> 30);
        x *= 0xBF58476D1CE4E5B9L;
        x ^= (x >>> 27);
        x *= 0x94D049BB133111EBL;
        x ^= (x >>> 31);
        return x;
    }

    @Override
    public double normalize(long value) {
        return (value & 0x7FFFFFFFFFFFFFFFL) / (double) Long.MAX_VALUE;
    }
}
