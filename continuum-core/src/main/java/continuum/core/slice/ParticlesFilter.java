package continuum.core.slice;

import continuum.atom.Atom;
import continuum.atom.Particles;
import continuum.slice.Filter;

/**
 * Filter non-matching partices in slices
 * Created by zack on 2/23/16.
 */
public class ParticlesFilter implements Filter {

    private final Particles particles;

    public ParticlesFilter(Particles particles) {
        this.particles = particles;
    }

    @Override
    public Action filter(Atom atom) {
        if (particles == null) return Action.CONTINUE;
        if (atom.particles() == null) return Action.SKIP;

        for (String name : particles.keySet()) {
            String atomVal = atom.particles().get(name);
            String sliceVal = particles.get(name);
            if (sliceVal == null) continue;
            if (atomVal == null) return Action.SKIP;
            if (!atomVal.equals((sliceVal))) return Action.SKIP;
        }

        return Action.CONTINUE;
    }
}
