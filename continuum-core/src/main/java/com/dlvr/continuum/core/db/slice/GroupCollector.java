package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.collect.Tree;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.SliceResult;
import com.dlvr.continuum.util.Strings;
import com.dlvr.continuum.util.datetime.Interval;

import java.util.ArrayList;
import java.util.List;

/**
 * Results grouped by particles
 * Created by zack on 2/25/16.
 */
public class GroupCollector implements Collector {

    private final String[] groups;
    private final Interval interval;
    private final Function function;

    private final Tree<Collector> collectors = new Tree<>();
    private final Collector stats;

    public GroupCollector(final String[] groups, final Interval interval, final Function function) {
        this.groups = groups;
        this.interval = interval;
        this.function = function;
        this.stats = new StatsCollector(function);
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
        //for (String group : collectors.keySet()) {
        collectors.each((s, v) -> {
            if (v != null) {
                NSliceResult res = (NSliceResult) v.result();
                res.name = s;
                children.add(res);
            }
        });

        /*
            children = collectors.values().stream().map(
                Collector::result
            ).collect(java.util.stream.Collectors.toList());
        */

        return Continuum
                .result(String.join(",", groups))
                .children(children)
                .values(stats.result().values())
                .build();
    }

    private Collector createSubCollector() {
        Collector collector = null;

        if (groups.length > 1) {
            String[] subGroup = new String[groups.length - 1];
            for (int i = 0; i < groups.length - 1; i++)
                subGroup[i] = groups[i];
            collector = Collectors.group(subGroup, interval, function);
        }
        else if (interval != null) {
            collector = Collectors.interval(interval, function);
        } else {
            collector = Collectors.stats(function);
        }
        return collector;
    }

    private String key(Atom atom) {
        return String.join(".", keys(atom));
    }

    /**
     * tag values for this key (groups)
     * @param atom to compute
     * @return keys
     */
    private String[] keys(Atom atom) {
        String[] keys = new String[groups.length];

        int i = 0;
        for (String group : groups) {
            String val = atom.particles().get(group);
            if (!Strings.empty(val)) {
                keys[i++] = val;
            }
        }

        return keys;
    }
}
