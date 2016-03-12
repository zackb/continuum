package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.SliceResult;
import com.dlvr.continuum.util.Strings;
import com.dlvr.continuum.util.datetime.Interval;

import java.util.*;

/**
 * Results grouped by particles
 * Created by zack on 2/25/16.
 */
public class GroupCollector implements Collector {

    private final String[] groups;
    private final Interval interval;
    private final Function function;

    private static final String DELIM = ",";

    private final Map<String, Collector> collectors = new HashMap<>();
    private final Collector stats;

    public GroupCollector(final String[] groups, final Interval interval, final Function function) {
        this.groups = groups;
        this.interval = interval;
        this.function = function;
        this.stats = new StatsCollector(function);
    }

    @Override
    public SliceResult result() {

        List<SliceResult> children = new ArrayList<>(collectors.size());

        collectors.keySet()
                .stream()
                .sorted((s1, s2) -> s1.length() == s2.length() ? 0 : s1.length() > s2.length() ? 1 : -1)
                .forEach(key -> {
                    NSliceResult res = (NSliceResult)collectors.get(key).result();
                    res.name = key;
                    children.add(res);
                });

        return Continuum
                .result(String.join(DELIM, groups))
                .children(children)
                .values(stats.result().values())
                .build();
    }

    private Collector createSubCollector(String[] parts) {
        Collector collector = null;

        if (parts.length > 1)
            collector = Collectors.group(parts, interval, function);
        else if (interval != null)
            collector = Collectors.interval(interval, function);
        else
            collector = Collectors.stats(function);
        return collector;
    }

    private String key(Atom atom) {
        return String.join(DELIM, keys(atom));
    }

    @Override
    public void collect(Atom atom) {
        String[] parts = keys(atom);

        stats.collect(atom);
        for (int i = parts.length; i > 0; i--) {
            List<String> ss = Arrays.asList(parts).subList(0, i);
            String k = String.join(DELIM, ss);
            Collector collector = collectors.get(k);
            if (collector == null) {
                collector = createSubCollector(Strings.range(groups, 0, i - 1));
                collectors.put(k, collector);
            }
            collector.collect(atom);
        }
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

        if (keys.length > i) return Strings.range(keys, 0, i);
        else return keys;
    }
}
