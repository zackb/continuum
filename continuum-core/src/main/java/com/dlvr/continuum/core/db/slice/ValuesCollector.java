package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Values;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.Slice;
import com.dlvr.continuum.util.Strings;

/**
 * Collect Values
 * Created by zack on 2/11/16.
 */
public class ValuesCollector implements Collector {

    private final String name;
    private double max = Double.MIN_VALUE;
    private double min = Double.MAX_VALUE;
    private long count = 0;
    private double sum = 0.0d;

    private final Function function;
    private long timestamp = 0L;

    public ValuesCollector(Function function) {
        this("values", function);
    }

    public ValuesCollector(String name, Function function) {
        this.name = name;
        this.function = function;
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

    @Override
    public String name() {
        return name;
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
        throw new Error("Not implemented: " + function);
    }

    public double sum() {
        return sum;
    }

    public long count() {
        return count;
    }

    @Override
    public Slice result() {
        if (min == Double.MAX_VALUE) min = 0;
        if (max == Double.MIN_VALUE) max = 0;
        double value;


        Function fxn = function != null ? function : Function.AVG;

        switch (fxn) {
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
                throw new Error("Not implemented: " + fxn);
        }

        Values values = Continuum.values()
                .min(min)
                .max(max)
                .count(count)
                .sum(sum)
                .value(value)
                .build();

        return Continuum.result(name != null ? name : fxn.name().toLowerCase())
                .values(values)
                .timestamp(timestamp)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValuesCollector)) return false;

        ValuesCollector that = (ValuesCollector) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (function != that.function) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        temp = Double.doubleToLongBits(max);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(min);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (count ^ (count >>> 32));
        temp = Double.doubleToLongBits(sum);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (function != null ? function.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return Strings.sprintf("%s, %f,%f,%d,%f,%s,%d",
            name,
            max,
            min,
            count,
            sum,
            function,
            timestamp
        );
    }
}
