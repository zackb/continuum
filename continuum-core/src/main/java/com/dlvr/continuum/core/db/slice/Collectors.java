package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Slice;

/**
 * Slice atom collector factory and utils
 */
public class Collectors {
    public static Collector forSlice(Slice slice) {

        if (slice.interval() != null)
            return new IntervalCollector(slice);

        return new StatsCollector(slice);
    }
}
