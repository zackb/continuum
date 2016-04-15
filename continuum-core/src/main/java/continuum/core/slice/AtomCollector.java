package continuum.core.slice;

import continuum.Continuum;
import continuum.atom.Atom;
import continuum.slice.Collector;
import continuum.slice.Function;
import continuum.slice.Slice;
import continuum.util.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collect atoms matching the scan filter. For when neither function nor interval is specified
 * Created by zack on 2/24/16.
 */
public class AtomCollector implements Collector<Atom> {

    public final List<Atom> atoms;
    private final ValuesCollector values;
    private final String name;
    private final int limit;

    private static final int LIMIT_NONE = 0;

    public AtomCollector() {
        this("atoms", Function.AVG, LIMIT_NONE);
    }

    public AtomCollector(String name) {
        this(name, Function.AVG, LIMIT_NONE);
    }

    public AtomCollector(Function function) {
        this("atoms", function, LIMIT_NONE);
    }

    public AtomCollector(Function function, Integer limit) {
        this("atoms", function, limit);
    }

    public AtomCollector(String name, Function function) {
        this(name, function, LIMIT_NONE);
    }

    public AtomCollector(String name, Function function, Integer limit) {
        this.name = name;
        this.atoms = new ArrayList<>();
        this.values = new ValuesCollector(function == null ? Function.AVG : function);
        if (limit != null && limit > 0) this.limit = limit;
        else this.limit = LIMIT_NONE;
    }

    @Override
    public void collect(Atom atom) {
        if (limit == LIMIT_NONE || atoms.size() < limit)
            atoms.add(atom);
        values.collect(atom);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Slice slice() {
        Collections.sort(atoms, (o1, o2) -> o2.timestamp().compareTo(o1.timestamp()));
        return Continuum.result(name())
                .values(values.slice().values())
                .atoms(atoms)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AtomCollector)) return false;

        AtomCollector that = (AtomCollector) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = atoms != null ? atoms.hashCode() : 0;
        result = 31 * result + (values != null ? values.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.sprintf("%s,%d", name, atoms != null ? atoms.size() : 0);
    }
}
