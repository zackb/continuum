package com.dlvr.continuum.core.atom;

import com.dlvr.continuum.core.db.KAtomID;
import com.dlvr.continuum.db.AtomID;

/**
 * An atom in a time key value continuum
 * Created by zack on 2/12/16.
 */
public class KAtom extends NAtom {

    public KAtom(String name, NTags tags, long timestamp, NFields fields, double value) {
        super(name, tags, timestamp, fields, value);
    }

    @Override
    public AtomID ID() {
        // TODO: Cache? Mutability?
        return new KAtomID(this);
    }
}
