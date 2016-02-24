package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Slice;
import com.dlvr.continuum.db.slice.SliceResult;

import java.util.*;

/**
 * Collect sub-aggregated data at a given interval
 * Created by zack on 2/19/16.
 */
public class IntervalCollector implements Collector {

    private final Slice slice;
    final Map<Long, StatsCollector> collectors = new HashMap<>();
    final StatsCollector collector;
    private long timestamp = 0L;

    public IntervalCollector(Slice slice) {
        this.slice = slice;
        this.collector = new StatsCollector(slice);
    }

    @Override
    public void collect(Atom atom) {

        if (timestamp == 0L) timestamp = atom.timestamp();

        long ts = atom.timestamp();
        long bucket = ts - (ts % slice.interval().toMillis());

        if (collectors.get(bucket) == null) collectors.put(bucket, new StatsCollector(slice));

        collectors.get(bucket).collect(atom);
        collector.collect(atom);
    }

    @Override
    public SliceResult result() {

        SliceResult result = collector.result();
        List<Long> sorted = new ArrayList<>(collectors.keySet());
        Collections.sort(sorted, (o1, o2) -> o2.compareTo(o1));
        for (Long ts : sorted) {
            SliceResult child = collectors.get(ts).result();
            result.addChild(child);
        }
        return result;
    }
}
