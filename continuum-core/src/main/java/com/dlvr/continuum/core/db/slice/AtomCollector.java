package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Collector;
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

    public AtomCollector() {
        atoms = new ArrayList<>();
    }

    @Override
    public void collect(Atom atom) {
        atoms.add(atom);
    }

    @Override
    public SliceResult result() {
        Collections.sort(atoms, (o1, o2) -> o2.timestamp().compareTo(o1.timestamp()));
        return Continuum.result("atoms")
                .value((double)atoms.size())
                .atoms(atoms)
                .build();
    }
}