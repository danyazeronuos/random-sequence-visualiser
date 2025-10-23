package com.danyazero.cs1.utils;

public class HistogramUtility {
    public static int[] build(double[] data, int bins) {
        var histogram = new int[bins];

        for (double value : data) {
            var index = (int) (value * (double) bins);
            if (index == bins) {
                index--;
            }
            histogram[index]++;
        }

        return histogram;
    }
}
