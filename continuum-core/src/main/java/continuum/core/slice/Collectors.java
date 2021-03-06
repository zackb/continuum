package continuum.core.slice;

import continuum.slice.Collector;
import continuum.slice.Function;
import continuum.slice.Scan;
import continuum.util.datetime.Interval;

/**
 * Scan atom collector factory and utils
 */
public class Collectors {

    public static Collector forScan(Scan scan) {

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
            collector = values(
                scan.function()
            );
        else
            collector = atoms(scan.function(), scan.limit());

        return collector;
    }

    /**
     * Group scan results on one or more particles. Respects intervals
     * @param name to refer to group
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
     * @param name to refer to collector
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
     * @return new values collector
     */
    public static ValuesCollector values(Function function) {
        return new ValuesCollector(function);
    }

    /**
     * Collect values min,max,sum,count,value
     * @param name to refer to the values collection
     * @param function to report on in addition to standard 5 values
     * @return new values collector
     */
    public static ValuesCollector values(String name, Function function) {
        return new ValuesCollector(name, function);
    }

    /**
     * Collect values min,max,sum,count,value
     * @param name to refer to the values collection
     * @param function to report on in addition to standard 5 values
     * @param timestamp convenience to override the timestamp for the collector to use. defaults to first atom collected
     * @return new values collector
     */
    public static ValuesCollector values(String name, Function function, long timestamp) {
        return new ValuesCollector(name, function, timestamp);
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
     * @param name to refer to the atom collection
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
     * @param function to apply
     * @return new raw atom collector
     */
    public static AtomCollector atoms(Function function, Integer limit) {
        return new AtomCollector(function, limit);
    }

    /**
     * Raw atom collector. Used instead of aggregations
     * @param name to refer to the atom collection
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
