package com.dlvr.continuum.core.atom;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Fields;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.atom.Values;

/**
 * An atom in the continuum
 */
public abstract class NAtom implements Atom {
    public String name;
    public NParticles particles;
    public Long timestamp;
    public NFields fields;
    public Values values;

    public NAtom() { }

    public NAtom(String name, NParticles particles, long timestamp, NFields fields, Values values) {
        this.name = name;
        this.particles = particles;
        this.timestamp = timestamp;
        this.fields = fields;
        this.values = values;
    }

    @Override
    public String name() {
        return name;
    }
    @Override
    public Particles particles() {
        return particles;
    }
    @Override
    public Fields fields() {
        return fields;
    }
    @Override
    public Values values() {
        return values;
    }
    @Override
    public Long timestamp() {
        return timestamp;
    }
}
