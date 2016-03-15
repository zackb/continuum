package com.dlvr.continuum.core.atom;

import com.dlvr.continuum.core.db.SAtomID;
import com.dlvr.continuum.db.AtomID;

/**
 * An atom in a time key values continuum
 * Created by zack on 2/12/16.
 */
public class SAtom extends NAtom {

    // for serde
    public SAtom() { }

    public SAtom(String name, NParticles particles, long timestamp, NFields fields, NValues values) {
        super(name, particles, timestamp, fields, values);
    }

    @Override
    public AtomID ID() {
        // TODO: Cache? Mutability?
        return new SAtomID(this);
    }
}
