package continuum.core.slice;

import continuum.atom.Atom;
import continuum.slice.Collector;
import continuum.slice.Function;
import continuum.slice.Slice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Convenience collector to wrap a list of collectors
 * Created by zack on 3/11/16.
 */
public class AndCollector implements Collector<Atom> {

    private final Collector[] collectors;

    private final Collector values;

    public AndCollector(Collector... collectors) {
        this.collectors = collectors;
        this.values = Collectors.values(Function.AVG);
    }

    public AndCollector(Collector collector, Collector... collectors) {
        this.values = Collectors.values(Function.AVG);
        List<Collector> list = Arrays.asList(collectors);
        list.add(collector);
        this.collectors = list.toArray(collectors);
    }

    @Override
    public void collect(Atom atom) {
        for (Collector collector : collectors) {
            collector.collect(atom);
        }
    }

    @Override
    public String name() {
        return "and";
    }

    @Override
    public Slice slice() {
        Slice result = values.slice();
        for (Collector collector : collectors) {
            result.add(collector.slice());
        }
        return result;
    }

    public Collector[] collectors() {
        return collectors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AndCollector)) return false;

        AndCollector that = (AndCollector) o;

        return Arrays.equals(collectors, that.collectors);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(collectors);
    }
}
