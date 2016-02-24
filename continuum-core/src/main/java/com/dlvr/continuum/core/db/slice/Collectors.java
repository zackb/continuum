package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Slice;

/**
 * Slice atom collector factory and utils
 */
public class Collectors {

    public static Collector forSlice(Slice slice) {
        Collector collector = null;
        if (slice.interval() != null) {
            collector = new IntervalCollector(slice);
        } else if (slice.function() != null) {
            collector = new StatsCollector(slice);
        } else {
            collector = new AtomCollector();
        }

        return collector;
    }
}
