package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.SliceResult;
import com.dlvr.continuum.util.Strings;
import com.dlvr.continuum.util.datetime.Interval;

import java.util.*;

/**
 * Collect sub-aggregated data at a given interval
 * Created by zack on 2/19/16.
 */
public class IntervalCollector implements Collector {

    private final String name;
    private final Interval interval;
    private final Function function;
    final Map<Long, StatsCollector> collectors = new HashMap<>();
    final Collector collector;
    private long timestamp = 0L;

    public IntervalCollector(Interval interval) {
        this("hist", interval, Function.AVG);
    }

    public IntervalCollector(Interval interval, Function function) {
        this("hist", interval, function);
    }

    public IntervalCollector(String name, Interval interval, Function function) {
        this.name = name;
        this.interval = interval;
        this.function = function;
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
    public String name() {
        return name;
    }

    @Override
    public SliceResult result() {

        NSliceResult result = (NSliceResult)collector.result();
        result.name = name();
        List<Long> sorted = new ArrayList<>(collectors.keySet());
        Collections.sort(sorted, (o1, o2) -> o2.compareTo(o1));
        for (Long ts : sorted) {
            SliceResult child = collectors.get(ts).result();
            result.addChild(child);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntervalCollector)) return false;

        IntervalCollector that = (IntervalCollector) o;

        if (timestamp != that.timestamp) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (interval != null ? !interval.equals(that.interval) : that.interval != null) return false;
        if (function != that.function) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (interval != null ? interval.hashCode() : 0);
        result = 31 * result + (function != null ? function.hashCode() : 0);
        result = 31 * result + (collectors != null ? collectors.hashCode() : 0);
        result = 31 * result + (collector != null ? collector.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return Strings.sprintf("%s,%s,%s,%d:: %s",
                name,
                interval,
                function,
                timestamp,
                collectors != null ? collectors.toString() : "()"
        );
    }
}
