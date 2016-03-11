package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Slice;

/**
 * Slice atom collector factory and utils
 */
public class Collectors {

    public static Collector forSlice(Slice slice) {

        Collector collector = null;

        if (slice.groups() != null)
            collector = group(slice);
        else if (slice.interval() != null)
            collector = interval(slice);
        else if (slice.function() != null)
            collector = stats(slice);
        else
            collector = atoms(slice);

        return collector;
    }

    /**
     * Group slice results on one or more particles. Respects intervals
     * @param slice to group results by
     * @return new grouping collector
     */
    public static GroupCollector group(Slice slice) {
        return new GroupCollector(slice);
    }

    /**
     * Bucket results into time intervals
     * @param slice to bucket
     * @return new bucketing time interval collector
     */
    public static IntervalCollector interval(Slice slice) {
        return new IntervalCollector(slice);
    }

    /**
     * Collect values min,max,sum,count,value
     * @param slice to collect
     * @return new stats collector
     */
    public static StatsCollector stats(Slice slice) {
        return new StatsCollector(slice);
    }

    /**
     * Raw atom collector. Used instead of aggregations
     * @param slice
     * @return new raw atom collector
     */
    public static AtomCollector atoms(Slice slice) {
        return new AtomCollector(slice);
    }
}
