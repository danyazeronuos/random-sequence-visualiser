package com.danyazero.cs1.model;

import java.util.function.BiConsumer;

public abstract class BatchGenerator {

    protected int batchSize;
    protected BiConsumer<int[], BatchGenerator> batchCallback;

    public BatchGenerator(int batchSize, BiConsumer<int[], BatchGenerator> batchCallback) {
        this.batchSize = batchSize;
        this.batchCallback = batchCallback;
    }

    public BatchGenerator() {
        this(0, null);
    }

    public abstract int[] generate(int size);
    public abstract double normalize(int value);

    public void setBatchCallback(BiConsumer<int[], BatchGenerator> batchCallback) {
        this.batchCallback = batchCallback;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
