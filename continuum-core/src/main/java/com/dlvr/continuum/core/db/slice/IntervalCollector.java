package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Slice;

import java.util.HashMap;
import java.util.Map;

/**
 * Collect sub-aggregated data at a given interval
 * Created by zack on 2/19/16.
 */
public class IntervalCollector implements Collector {

    private final Slice slice;
    final Map<Long, StatsCollector> collectors = new HashMap<>();

    public IntervalCollector(Slice slice) {
        this.slice = slice;
    }

    @Override
    public void collect(Atom atom) {

        long ts = atom.timestamp();
        long bucket = ts - (ts % slice.interval().toMillis(1));

        if (collectors.get(bucket) == null) collectors.put(bucket, new StatsCollector(slice));

        collectors.get(bucket).collect(atom);
    }

    @Override
    public Map<Long, Double> value() {
        Map<Long, Double> result = new HashMap<>(collectors.size());
        for (Long ts : collectors.keySet()) {
            Double value = collectors.get(ts).value().values().iterator().next();
            result.put(ts, value);
        }
        return result;
    }
}
