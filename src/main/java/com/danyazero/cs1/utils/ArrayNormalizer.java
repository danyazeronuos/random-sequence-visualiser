package com.danyazero.cs1.utils;

import com.danyazero.cs1.model.BatchGenerator;

public class ArrayNormalizer {

    public static double[] apply(long[] array, BatchGenerator generator) {
        var result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = generator.normalize(array[i]);
        }

        return result;
    }
}
