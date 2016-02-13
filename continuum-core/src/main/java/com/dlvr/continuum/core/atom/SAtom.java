package com.dlvr.continuum.core.atom;

import com.dlvr.continuum.core.db.SAtomID;
import com.dlvr.continuum.db.AtomID;

/**
 * An atom in a time key value continuum
 * Created by zack on 2/12/16.
 */
public class SAtom extends NAtom {

    public SAtom() { }

    public SAtom(String name, NTags tags, long timestamp, NFields fields, double value) {
        super(name, tags, timestamp, fields, value);
    }

    @Override
    public AtomID ID() {
        // TODO: Cache? Mutability?
        return new SAtomID(this);
    }
}
