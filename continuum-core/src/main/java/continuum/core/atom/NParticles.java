package continuum.core.atom;

import continuum.atom.ParticlesID;
import continuum.atom.Particles;

import java.util.*;

/**
 * Base Tags implementation
 */
public class NParticles extends HashMap<String, String> implements Particles {

    private transient List<String> sortedNames;

    NParticles() { super(); }

    public NParticles(Map<String, String> raw) {
        super(raw);
    }

    @Override
    public String get(String key) {
        return super.get(key);
    }

    @Override
    public List<String> names() {
        if (sortedNames == null) {
            List<String> sn = new ArrayList<>(keySet());
            Collections.sort(sn);
            sortedNames = sn;
        }
        return sortedNames;
    }

    @Override
    public String put(String key, String value) {
        return super.put(key, value);
    }

    @Override
    public ParticlesID ID() {
        // TODO: Cache? Mutability?
        return new NParticlesID(this);
    }
}
