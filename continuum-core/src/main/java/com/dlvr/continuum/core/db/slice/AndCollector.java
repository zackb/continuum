package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.SliceResult;

/**
 * Convenience collector to wrap a list of collectors
 * Created by zack on 3/11/16.
 */
public class AndCollector implements Collector {

    private final Collector[] collectors;

    private final Collector stats;

    public AndCollector(Collector... collectors) {
        this.collectors = collectors;
        this.stats = Collectors.stats(Function.AVG);
    }

    @Override
    public void collect(Atom atom) {
        for (Collector collector : collectors) {
            collector.collect(atom);
        }
    }

    @Override
    public SliceResult result() {
        SliceResult result = stats.result();
        for (Collector collector : collectors) {
            result.addChild(collector.result());
        }
        return result;
    }
}
