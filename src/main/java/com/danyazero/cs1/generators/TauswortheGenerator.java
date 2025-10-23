package com.danyazero.cs1.generators;

import com.danyazero.cs1.model.BatchGenerator;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class TauswortheGenerator extends BatchGenerator {
    private long s1;
    private long s2;
    private long s3;

    private long[] generatedValues;

    public TauswortheGenerator(long s1, long s2, long s3, int batchSize, BiConsumer<long[], BatchGenerator> batchCallback) {
        super(batchSize, batchCallback);
        this.s1 = s1 & 0xFFFFFFFFL;
        this.s2 = s2 & 0xFFFFFFFFL;
        this.s3 = s3 & 0xFFFFFFFFL;

        if (this.s1 < 2) this.s1 = 2;
        if (this.s2 < 8) this.s2 = 8;
        if (this.s3 < 16) this.s3 = 16;
    }

    public TauswortheGenerator(long s1, long s2, long s3) {
        this(s1, s2, s3, 0, null);
    }


    @Override
    public long[] generate(int size) {
        generatedValues = new long[size];
        for (int i = 0; i < size; i++) {
            var result = generate();
            generatedValues[i] = result;

            if (this.batchSize > 0 && (i + 1) % this.batchSize == 0) {
                this.batchCallback.accept(Arrays.copyOfRange(generatedValues, 0, i+1), this);
            }
        }

        return generatedValues;
    }

    @Override
    public double normalize(long value) {
        return  (value & 0xFFFFFFFFL) / 4294967296.0;
    }

    public long generate() {
        s1 = (((s1 & 0xFFFFFFFEL) << 12) ^ (((s1 << 13) ^ s1) >>> 19)) & 0xFFFFFFFFL;
        s2 = (((s2 & 0xFFFFFFF8L) << 4) ^ (((s2 << 2) ^ s2) >>> 25)) & 0xFFFFFFFFL;
        s3 = (((s3 & 0xFFFFFFF0L) << 17) ^ (((s3 << 3) ^ s3) >>> 11)) & 0xFFFFFFFFL;


        return (s1 ^ s2 ^ s3) ;
    }

}
