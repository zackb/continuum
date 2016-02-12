package com.dlvr.continuum.series.query.impl;

import com.dlvr.continuum.series.query.Function;

/**
 * Query result collector
 * Created by zack on 2/11/16.
 */
public class Collector {
    private double max = 0d;
    private double min = 0d;
    private long count = 0;
    private double sum = 0.0d;
    public void collect(double value) {
        if (value > max) max = value;
        if (value < min) min = value;
        sum += value;
        count++;
    }

    public double max() {
        return max;
    }

    public double min() {
        return min;
    }

    public double avg() {
        return sum / count;
    }

    public double stddev() {
        throw new Error("Not implemented");
    }

    public double sum() {
        return sum;
    }

    public double value(Function function) {

        double value;

        switch (function) {
            case AVG:
                value = avg();
                break;
            default:
                throw new Error("Not implemented: " + function);
        }

        return value;
    }
}
