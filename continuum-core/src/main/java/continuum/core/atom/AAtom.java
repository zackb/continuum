package continuum.core.atom;

import continuum.atom.Atom;
import continuum.atom.Fields;
import continuum.atom.Particles;
import continuum.atom.Values;

import static continuum.Continuum.Dimension;

/**
 * An atom in the continuum
 */
public abstract class AAtom implements Atom {
    public String name;
    public AParticles particles;
    public Long timestamp;
    public AFields fields;
    public AValues values;
    public Dimension dimension;

    public AAtom() { }

    public AAtom(String name, AParticles particles, long timestamp, AFields fields, AValues values) {
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
    @Override
    public Dimension dimension() {
        return dimension;
    }
}
