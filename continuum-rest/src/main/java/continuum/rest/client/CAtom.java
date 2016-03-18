package continuum.rest.client;

import continuum.atom.Atom;
import continuum.atom.AtomID;
import continuum.core.atom.*;

/**
 * Basic POJO atom DTO
 * Created by zack on 2/26/16.
 */
public class CAtom extends NAtom implements Atom {

    public CAtom(String name, NParticles particles, long timestamp, NFields fields, NValues values) {
        super(name, particles, timestamp, fields, values);
    }

    @Override
    public AtomID ID() {
        return new SAtomID(this); // TODO: ? does this matter? CAtomID?
    }
}
