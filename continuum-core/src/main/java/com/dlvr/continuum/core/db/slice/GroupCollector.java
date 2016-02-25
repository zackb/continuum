package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Slice;
import com.dlvr.continuum.db.slice.SliceResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Results grouped by particles
 * Created by zack on 2/25/16.
 */
public class GroupCollector implements Collector {

    private final Slice slice;

    private final Map<String, Collector> collectors = new HashMap<>();
    private final Collector stats;

    public GroupCollector(Slice slice) {
        this.slice = slice;
        this.stats = new StatsCollector(slice);
    }

    @Override
    public void collect(Atom atom) {
        String key = key(atom);
        Collector collector = collectors.get(key);
        if (collector == null) {
            collector = createSubCollector();
            collectors.put(key, collector);
        }
        collector.collect(atom);
        stats.collect(atom);
    }

    @Override
    public SliceResult result() {

        List<SliceResult> children = new ArrayList<>(collectors.size());
        for (String group : collectors.keySet()) {
            NSliceResult res = (NSliceResult)collectors.get(group).result();
            res.name = group;
            children.add(res);
        }
        /*
            children = collectors.values().stream().map(
                Collector::result
            ).collect(java.util.stream.Collectors.toList());
        */

        return Continuum
                .result(String.join(",", slice.groups()))
                .children(children)
                .values(stats.result().values())
                .build();
    }

    private Collector createSubCollector() {
        Collector collector = null;
        if (slice.interval() != null) {
            collector = new IntervalCollector(slice);
        } else {
            collector = new StatsCollector(slice);
        }
        return collector;
    }

    private String key(Atom atom) {

        String key = "";

        for (String group : slice.groups()) {
            key += atom.particles().get(group) + ",";
        }

        return key.substring(0, key.length() - 1);
    }
}
