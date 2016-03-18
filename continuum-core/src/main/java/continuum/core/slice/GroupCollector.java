package continuum.core.slice;

import continuum.Continuum;
import continuum.atom.Atom;
import continuum.tree.Tree;
import continuum.slice.Collector;
import continuum.slice.Function;
import continuum.slice.Slice;
import continuum.util.Strings;
import continuum.util.datetime.Interval;

import java.util.*;

/**
 * Results grouped by particles
 * Created by zack on 2/25/16.
 */
public class GroupCollector implements Collector<Atom> {

    private final String[] groups;
    private final Interval interval;
    private final Function function;
    private final String name;

    private static final char DELIM = ':';
    private static final String SDELIM = "" + DELIM;

    private final Tree<Collector> collectors;
    private final Collector values;

    public GroupCollector(final String name, final String[] groups, final Interval interval, final Function function) {
        this.groups = groups;
        this.interval = interval;
        this.function = function;
        this.values = Collectors.values(function);
        this.collectors = new Tree<>(this);
        this.name = name;
    }

    @Override
    public Slice slice() {
        List<Slice> children = new ArrayList<>();
        final Slice g = Continuum
                .result(name())
                .children(children)
                .values(values.slice().values())
                .build();

        collectors
                .children()
                .stream()
                .map(GroupCollector::result)
                .forEach(g::add);

        return g;
    }

    public static Slice result(Tree<Collector> stree) {
        ASlice s = (ASlice) stree.data.slice();
        s.name = stree.data.name();

        // prefix name with names of all parents
        // TODO: functional!
        for (Tree<Collector> cur =
                stree.parent();
                    cur != null;
                        s.name = cur.data.name() + DELIM + s.name,
                        cur = cur.parent());

        stree.children()
             .stream()
             .map(GroupCollector::result)
             .forEach(s::add);
        return s;
    }

    @Override
    public void collect(Atom atom) {

        values.collect(atom);
        String[] keys = keys(atom);

        Tree<Collector> current = collectors;
        for (int i = 0; i < keys.length; i++) {
            current = current.child(subCollector(keys[i]));
            current.data.collect(atom);
        }
    }

    private Collector subCollector(String name) {
        Collector c = null;
        if (interval != null) c = Collectors.interval(name, interval, function);
        else if (function != null) c = Collectors.values(name, function);
        else c = Collectors.atoms(name);
        return c;
    }

    @Override
    public String name() {
        return name;
    }

    /**
     * tag values for this key (groups)
     * @param atom to compute
     * @return keys
     */
    private String[] keys(Atom atom) {
        String[] keys = new String[groups.length];

        int i = 0;
        for (String group : groups) {
            String val = atom.particles().get(group);
            if (!Strings.empty(val)) {
                keys[i++] = val;
            }
        }

        return keys.length > i ? Strings.range(keys, 0, i) : keys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupCollector)) return false;

        GroupCollector that = (GroupCollector) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(groups, that.groups)) return false;
        if (interval != null ? !interval.equals(that.interval) : that.interval != null) return false;
        if (function != that.function) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(groups);
        result = 31 * result + (interval != null ? interval.hashCode() : 0);
        result = 31 * result + (function != null ? function.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (collectors.hashCode());
        result = 31 * result + (values.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return Strings.sprintf("%s,%s,%s",
                name,
                interval,
                function != null ? function : "raw"
        );
    }
}
