package com.dlvr.continuum.core.atom;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Fields;
import com.dlvr.continuum.atom.Tags;
import com.dlvr.continuum.db.AtomID;
import com.dlvr.continuum.core.db.SAtomID;

/**
 * Base Atom implementation
 */
public class NAtom implements Atom {
    public String name;
    public NTags tags;
    public Long timestamp;
    public NFields fields;
    public double value;

    public NAtom() { }

    public NAtom(String name, NTags tags, long timestamp, NFields fields, double value) {
        this.name = name;
        this.tags = tags;
        this.timestamp = timestamp;
        this.fields = fields;
        this.value = value;
    }

    @Override
    public String name() {
        return name;
    }
    @Override
    public Tags tags() {
        return tags;
    }
    @Override
    public Fields fields() {
        return fields;
    }
    @Override
    public double value() {
        return value;
    }
    @Override
    public Long timestamp() {
        return timestamp;
    }

    @Override
    public AtomID ID() {
        // TODO: Cache? Mutability?
        return new SAtomID(this);
    }
}
