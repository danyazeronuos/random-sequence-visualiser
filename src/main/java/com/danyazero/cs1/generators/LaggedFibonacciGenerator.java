package com.danyazero.cs1.generators;

import com.danyazero.cs1.model.BatchGenerator;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class LaggedFibonacciGenerator extends BatchGenerator {
    private final long[] seeds;
    private long max;
    private final int S;
    private final int R;

    public LaggedFibonacciGenerator(long[] seeds, int S, int batchSize, BiConsumer<long[], BatchGenerator> batchCallback) {
        super(batchSize, batchCallback);
        if (!(S > 0 && S < seeds.length)) throw new IllegalArgumentException("S must satisfy 0 < S < R");

        this.seeds = seeds.clone();
        this.R = seeds.length;
        this.S = S;
    }

    public LaggedFibonacciGenerator(long[] seeds, int S) {
        this(seeds, S, 0, null);
    }

    @Override
    public long[] generate(int size) {
        max = Long.MIN_VALUE;
        int j = this.S - 1, k = this.R - 1;
        long[] X = this.seeds.clone();

        long[] result = new long[size];
        int batchPos = 0;

        for (int i = 0; i < size; i++) {
            if (i < X.length) {
                result[i] = X[i];
            } else {
                result[i] = X[k] = X[k] ^ X[j--];
                k--;
                if (j < 0) j = X.length - 1;
                if (k < 0) k = X.length - 1;
            };
            if (result[i] > max) max = result[i];

            if (this.batchSize > 0) {
                batchPos++;
                if (batchPos == this.batchSize) {
                    if (this.batchCallback != null) {
                        this.batchCallback.accept(Arrays.copyOfRange(result, 0, i + 1), this);
                    }
                    batchPos = 0;
                }
            }
        }

        return result;
    }

    @Override
    public double normalize(long value) {
        return  ((double) value / max);
    }
}
