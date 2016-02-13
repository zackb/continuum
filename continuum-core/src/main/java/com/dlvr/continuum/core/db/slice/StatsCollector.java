package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.db.Iterator;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Slice;

/**
 * Atom slice collector
 * Created by zack on 2/11/16.
 */
public class StatsCollector implements Collector {

    private double max = 0d;
    private double min = 0d;
    private long count = 0;
    private double sum = 0.0d;

    private final Slice slice;

    public StatsCollector(Slice slice) {
        this.slice = slice;
    }

    @Override
    public void collect(Iterator iterator) {
        double value = iterator.value();
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

    public double value() {

        double value;

        switch (slice.function()) {
            case AVG:
                value = avg();
                break;
            default:
                throw new Error("Not implemented: " + slice.function());
        }

        return value;
    }
}
