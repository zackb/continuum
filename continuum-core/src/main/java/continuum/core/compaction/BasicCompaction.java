package continuum.core.compaction;

import continuum.Continuum;
import continuum.atom.Atom;
import continuum.atom.Particles;
import continuum.compaction.Compaction;
import continuum.compaction.RetentionPolicy;
import continuum.core.slice.Collectors;
import continuum.core.slice.ValuesCollector;
import continuum.except.ZiggyStardustError;
import continuum.slab.Iterator;
import continuum.slice.Function;
import continuum.util.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic implementation of compaction which ignores any business logic and is only responsible for combining
 * data points in which ALL Particles match. Fields are destroyed.
 */
public class BasicCompaction implements Compaction {

    private final Continuum cntm;
    private final Iterator<Atom> iterator;
    private final RetentionPolicy retentionPolicy;

    public BasicCompaction(Continuum continuum, RetentionPolicy retentionPolicy) {
        this.cntm = continuum;
        this.iterator = continuum.translator().iterator();
        this.retentionPolicy = retentionPolicy;
    }

    @Override
    public void compact() throws Exception {
        iterator.seekToFirst();

        Atom prev = null;
        List<Atom> batch = new ArrayList<>();

        while (iterator.valid()) {

            Atom atom = iterator.get();

            if (!match(prev, atom)) {
                writeCompaction(fuse(batch), batch);
                batch.clear();
                batch.add(atom);
            } else {
                batch.add(atom);
            }

            prev = atom;

            iterator.next();
        }
    }

    private void writeCompaction(Atom atom, List<Atom> atoms) throws Exception {
        for (Atom a : atoms) {
            cntm.delete(a);
        }
        cntm.write(atom); // unfortunately, have to do this after because atoms[0] will have the same AtomID
    }

    static Atom fuse(List<Atom> atoms) {

        ValuesCollector collector = Collectors.values(Function.AVG);

        Atom template = atoms.get(0);

        atoms.forEach(collector::collect);

        Continuum.AtomBuilder builder = null;

        if (template.dimension() == Continuum.Dimension.SPACE)
            builder = Continuum.satom();
        else if (template.dimension() == Continuum.Dimension.TIME)
            builder = Continuum.katom();
        else throw new ZiggyStardustError();

        return builder
                .name(template.name())
                .particles(template.particles())
                .values(Continuum.values(
                            collector.min(),
                            collector.max(),
                            collector.count(),
                            collector.sum(),
                            collector.avg()))
                .timestamp(template.timestamp())
                .build();
    }

    /**
     * See if two atoms are able to be fused.
     * TODO: Compare the ID bytes for huge speed gain! KISS for now
     * @param a1 atom to compare
     * @param a2 atom to compare
     * @return true if the name and all particles names and values match
     */
    static boolean match(Atom a1, Atom a2) {
        // check that we're not at the beginning
        if (a1 == null || a2 == null) return false;

        // check atom names match
        if (!Strings.equals(a1.name(), a2.name())) return false;

        // check atom particles match
        Particles p1 = a1.particles();
        Particles p2 = a2.particles();

        // if both have no particles OK else false
        if (p1 == null || p2 == null)
            if (p1 != null || p2 != null) return false;

        if (p1 != null && p2 != null) {
            if (p1.size() != p2.size()) return false;

            List<String> n1 = p1.names();
            List<String> n2 = p2.names();
            // check all keys match
            if (!n1.equals(n2)) return false;
            // check all values match
            for (String k1 : n1) {
                if (!p1.get(k1).equals(p2.get(k1))) return false;
            }
        }

        return true;
    }
}
