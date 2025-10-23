package com.danyazero.cs1.model;

import java.util.function.BiConsumer;

public abstract class BatchGenerator {

    protected int batchSize;
    protected BiConsumer<long[], BatchGenerator> batchCallback;

    public BatchGenerator(int batchSize, BiConsumer<long[], BatchGenerator> batchCallback) {
        this.batchSize = batchSize;
        this.batchCallback = batchCallback;
    }

    public BatchGenerator() {
        this(0, null);
    }

    public abstract long[] generate(int size);
    public abstract double normalize(long value);

    public void setBatchCallback(BiConsumer<long[], BatchGenerator> batchCallback) {
        this.batchCallback = batchCallback;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
