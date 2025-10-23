package com.danyazero.cs1.utils;

import com.danyazero.cs1.model.BatchGenerator;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class LinearGenerator extends BatchGenerator {
    private static final int k1 = 25_173;
    private static final int k0 = 13_849;
    private static final int m = 65_536;

    private final int seed;

    public LinearGenerator(int seed, int batchSize, BiConsumer<int[], BatchGenerator> batchCallback) {
        super(batchSize, batchCallback);
        this.seed = seed;
    }

    public LinearGenerator(int seed) {
        this(seed, 0, null);
    }

    @Override
    public int[] generate(int size) {
        int[] generatedValues = new int[size];
        int value = seed;

        for (int i = 0; i < size; i++) {
            value = generateValue(value);
            generatedValues[i] = value;

            if (this.batchSize > 0 && i % this.batchSize == 0) {
                int[] batch = Arrays.copyOfRange(generatedValues, 0, i);
                this.batchCallback.accept(batch, this);
            }
        }

        return generatedValues;
    }

    @Override
    public double normalize(int value) {
        return ((double) value) / m;
    }

    public static int generateValue(int v) {
        return (k1 * v + k0) % m;
    }
}
