package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.Scan;
import com.dlvr.continuum.util.datetime.Interval;

/**
 * Scan atom collector factory and utils
 */
public class Collectors {

    public static Collector forSlice(Scan scan) {

        Collector collector = null;

        if (scan.groups() != null)
            collector = group(
                scan.name(),
                scan.groups(),
                scan.interval(),
                scan.function()
            );
        else if (scan.interval() != null)
            collector = interval(
                scan.interval(),
                scan.function()
            );
        else if (scan.function() != null)
            collector = stats(
                scan.function()
            );
        else
            collector = atoms(scan.function());

        return collector;
    }

    /**
     * Group scan results on one or more particles. Respects intervals
     * @param name label for group
     * @param groups to grop by
     * @param interval to bucket groups
     * @param function to aggregate on
     * @return new grouping collector
     */
    public static GroupCollector group(String name, String[] groups, Interval interval, Function function) {
        return new GroupCollector(name, groups, interval, function);
    }

    /**
     * Bucket results into time intervals
     * @param interval to bucket
     * @return new bucketing time interval collector
     */
    public static IntervalCollector interval(Interval interval) {
        return new IntervalCollector(interval);
    }

    /**
     * Bucket results into time intervals
     * @param interval to bucket
     * @param function to apply
     * @return new bucketing time interval collector
     */
    public static IntervalCollector interval(Interval interval, Function function) {
        return new IntervalCollector(interval, function);
    }

    /**
     * Bucket results into time intervals
     * @param interval to bucket
     * @param function to apply
     * @return new bucketing time interval collector
     */
    public static IntervalCollector interval(String name, Interval interval, Function function) {
        return new IntervalCollector(name, interval, function);
    }

    /**
     * Collect values min,max,sum,count,value
     * @param function to report on in addition to standard 5 values
     * @return new stats collector
     */
    public static ValuesCollector stats(Function function) {
        return new ValuesCollector(function);
    }

    /**
     * Collect values min,max,sum,count,value
     * @param name for collection
     * @param function to report on in addition to standard 5 values
     * @return new stats collector
     */
    public static ValuesCollector stats(String name, Function function) {
        return new ValuesCollector(name, function);
    }

    /**
     * Raw atom collector. Used instead of aggregations
     * @return new raw atom collector
     */
    public static AtomCollector atoms() {
        return new AtomCollector();
    }

    /**
     * Raw atom collector. Used instead of aggregations
     * @param name for collection
     * @return new raw atom collector
     */
    public static AtomCollector atoms(String name) {
        return new AtomCollector(name);
    }

    /**
     * Raw atom collector. Used instead of aggregations
     * @param function to apply
     * @return new raw atom collector
     */
    public static AtomCollector atoms(Function function) {
        return new AtomCollector(function);
    }

    /**
     * Raw atom collector. Used instead of aggregations
     * @param name for collection
     * @param function to apply
     * @return new raw atom collector
     */
    public static AtomCollector atoms(String name, Function function) {
        return new AtomCollector(name, function);
    }

    /**
     * Convenience List collector
     * @param collectors to wrap
     * @return collector that calls collect() on all collectors
     */
    public static Collector and(Collector... collectors) {
        return new AndCollector(collectors);
    }
}
