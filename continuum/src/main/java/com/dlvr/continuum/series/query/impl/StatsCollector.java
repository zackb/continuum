package com.dlvr.continuum.series.query.impl;

import com.dlvr.continuum.series.db.Iterator;
import com.dlvr.continuum.series.query.Query;

/**
 * Query result collector
 * Created by zack on 2/11/16.
 */
public class StatsCollector implements ICollector {

    private double max = 0d;
    private double min = 0d;
    private long count = 0;
    private double sum = 0.0d;

    private final Query query;

    public StatsCollector(Query query) {
        this.query = query;
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

        switch (query.function()) {
            case AVG:
                value = avg();
                break;
            default:
                throw new Error("Not implemented: " + query.function());
        }

        return value;
    }
}
