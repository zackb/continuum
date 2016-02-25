package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Values;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.Slice;
import com.dlvr.continuum.db.slice.SliceResult;

/**
 * Atom slice collector
 * Created by zack on 2/11/16.
 */
public class StatsCollector implements Collector {

    private double max = Double.MIN_VALUE;
    private double min = Double.MAX_VALUE;
    private long count = 0;
    private double sum = 0.0d;

    private final Slice slice;
    private long timestamp = 0L;

    public StatsCollector(Slice slice) {
        this.slice = slice;
    }

    @Override
    public void collect(Atom atom) {
        double value = atom.values().value();
        if (value > max) max = value;
        if (value < min) min = value;
        if (timestamp == 0L) timestamp = atom.timestamp();
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

    public long count() {
        return count;
    }

    @Override
    public SliceResult result() {
        if (min == Double.MAX_VALUE) min = 0;
        if (max == Double.MIN_VALUE) max = 0;
        double value;

        Function function = slice.function();
        function = function != null ? function : Function.AVG;
        switch (function) {
            case AVG:
                value = avg();
                break;
            case MIN:
                value = min();
                break;
            case MAX:
                value = max();
                break;
            case STD:
                value = stddev();
                break;
            case SUM:
                value = sum();
                break;
            case COUNT:
                value = count();
                break;
            default:
                throw new Error("Not implemented: " + slice.function());
        }

        Values values = Continuum.values()
                .min(min)
                .max(max)
                .count(count)
                .sum(sum)
                .value(value)
                .build();

        return Continuum.result(function.name().toLowerCase())
                .values(values)
                .timestamp(timestamp)
                .build();
    }
}
