package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.SliceResult;
import com.dlvr.continuum.util.datetime.Interval;

import java.util.*;

/**
 * Collect sub-aggregated data at a given interval
 * Created by zack on 2/19/16.
 */
public class IntervalCollector implements Collector {

    private final Interval interval;
    private final Function function;
    final Map<Long, StatsCollector> collectors = new HashMap<>();
    final Collector collector;
    private long timestamp = 0L;

    public IntervalCollector(Interval interval) {
        this(interval, Function.AVG);
    }

    public IntervalCollector(Interval interval, Function function) {
        this.interval = interval;
        this.function = function == null ? Function.AVG : function;
        this.collector = new StatsCollector(function);
    }

    @Override
    public void collect(Atom atom) {

        if (timestamp == 0L) timestamp = atom.timestamp();

        long ts = atom.timestamp();
        long bucket = ts - (ts % interval.toMillis());

        if (collectors.get(bucket) == null) collectors.put(bucket, new StatsCollector(function));

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
