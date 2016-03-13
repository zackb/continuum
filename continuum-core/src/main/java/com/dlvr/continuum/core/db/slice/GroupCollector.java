package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.collect.Tree;
import com.dlvr.continuum.collect.Visitor;
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
    private final String name;

    private static final char DELIM = ',';
    private static final String SDELIM = "" + DELIM;

    private final Tree<Collector> collectors;
    private final Collector stats;

    public GroupCollector(final String[] groups, final String name, final Interval interval, final Function function) {
        this.groups = groups;
        this.interval = interval;
        this.function = function;
        this.stats = new StatsCollector(function);
        this.collectors = new Tree<>(this);
        this.name = name;
    }

    @Override
    public SliceResult result() {
        List<SliceResult> children = new ArrayList<>();
            //NSliceResult res = (NSliceResult)c.result();

        NSliceResult res = (NSliceResult)Continuum
                .result(String.join(SDELIM, groups))
                .children(children)
                .values(stats.result().values())
                .build();

        return res;

    }

    private String key(Atom atom) {
        return String.join(SDELIM, keys(atom));
    }

    @Override
    public void collect(Atom atom) {

        stats.collect(atom);
        collectors.accept(new Visitor<Collector>() {
            @Override
            public Visitor<Collector> visitTree(Tree<Collector> tree) {
                return null;
            }

            @Override
            public void visitData(Tree<Collector> parent, Collector data) {
                data.collect(atom);
            }
        });
    }

    @Override
    public String name() {
        return name;
    }

    class CollectVisitor implements Visitor<Collector> {

        @Override
        public Visitor<Collector> visitTree(Tree<Collector> tree) {
            return null;
        }

        @Override
        public void visitData(Tree<Collector> parent, Collector data) {

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
