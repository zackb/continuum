package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.Slice;
import com.dlvr.continuum.util.datetime.Interval;

/**
 * Slice atom collector factory and utils
 */
public class Collectors {

    public static Collector forSlice(Slice slice) {

        Collector collector = null;

        if (slice.groups() != null)
            collector = group(slice);
        else if (slice.interval() != null)
            collector = interval(slice.interval(), slice.function());
        else if (slice.function() != null)
            collector = stats(slice.function());
        else
            collector = atoms(slice.function());

        return collector;
    }

    /**
     * Group slice results on one or more particles. Respects intervals
     * @param slice to group results by
     * @return new grouping collector
     */
    public static GroupCollector group(Slice slice) {
        return new GroupCollector(slice.groups(), slice.interval(), slice.function());
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
     * Collect values min,max,sum,count,value
     * @param function to report on in addition to standard 5 values
     * @return new stats collector
     */
    public static StatsCollector stats(Function function) {
        return new StatsCollector(function);
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
     * @return new raw atom collector
     */
    public static AtomCollector atoms() {
        return new AtomCollector();
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
