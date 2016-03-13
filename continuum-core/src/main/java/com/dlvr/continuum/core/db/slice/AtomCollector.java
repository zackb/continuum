package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.SliceResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collect atoms matching the slice filter. For when neither function nor interval is specified
 * Created by zack on 2/24/16.
 */
public class AtomCollector implements Collector {

    public final List<Atom> atoms;
    private final StatsCollector stats;
    private final String name;

    public AtomCollector() {
        this("atoms", Function.AVG);
    }

    public AtomCollector(Function function) {
        this("atoms", function);
    }

    public AtomCollector(String name, Function function) {
        this.name = name;
        this.atoms = new ArrayList<>();
        this.stats = new StatsCollector(function == null ? Function.AVG : function);
    }

    @Override
    public void collect(Atom atom) {
        atoms.add(atom);
        stats.collect(atom);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public SliceResult result() {
        Collections.sort(atoms, (o1, o2) -> o2.timestamp().compareTo(o1.timestamp()));
        return Continuum.result("atoms")
                .values(stats.result().values())
                .atoms(atoms)
                .build();
    }
}
