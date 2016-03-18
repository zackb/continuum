package continuum.core.atom;

import continuum.atom.AtomID;

/**
 * An atom in a time key values continuum
 * Created by zack on 2/12/16.
 */
public class KAtom extends NAtom {

    // for serde
    public KAtom() { }

    public KAtom(String name, NParticles particles, long timestamp, NFields fields, NValues values) {
        super(name, particles, timestamp, fields, values);
    }

    @Override
    public AtomID ID() {
        // TODO: Cache? Mutability?
        return new KAtomID(this);
    }
}
