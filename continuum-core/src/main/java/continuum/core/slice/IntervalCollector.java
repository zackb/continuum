package continuum.core.slice;

import continuum.atom.Atom;
import continuum.slice.Collector;
import continuum.slice.Function;
import continuum.slice.Slice;
import continuum.util.Strings;
import continuum.util.datetime.Interval;

import java.util.*;

/**
 * Collect sub-aggregated data at a given interval
 * Created by zack on 2/19/16.
 */
public class IntervalCollector implements Collector<Atom> {

    private final String name;
    private final Interval interval;
    private final Function function;
    final Map<Long, ValuesCollector> collectors = new HashMap<>();
    final Collector values;

    public IntervalCollector(Interval interval) {
        this("interval", interval, Function.AVG);
    }

    public IntervalCollector(Interval interval, Function function) {
        this("interval", interval, function);
    }

    public IntervalCollector(String name, Interval interval, Function function) {
        this.name = name;
        this.interval = interval;
        this.function = function;
        this.values = Collectors.values(function);
    }

    @Override
    public void collect(Atom atom) {

        long ts = atom.timestamp();
        long bucket = ts - (ts % interval.millis());

        collectors.putIfAbsent(bucket, Collectors.values("values", function, bucket));

        collectors.get(bucket).collect(atom);
        values.collect(atom);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Slice slice() {

        ASlice result = (ASlice) values.slice();
        result.name = name();
        List<Long> sorted = new ArrayList<>(collectors.keySet());
        Collections.sort(sorted, (o1, o2) -> o2.compareTo(o1));
        for (Long ts : sorted) {
            ASlice child = (ASlice)collectors.get(ts).slice();
            child.timestamp = ts;
            child.name = new Date(child.timestamp).toString();
            result.add(child);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntervalCollector)) return false;

        IntervalCollector that = (IntervalCollector) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (interval != null ? !interval.equals(that.interval) : that.interval != null) return false;
        if (function != that.function) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (interval != null ? interval.hashCode() : 0);
        result = 31 * result + (function != null ? function.hashCode() : 0);
        result = 31 * result + (collectors != null ? collectors.hashCode() : 0);
        //result = 31 * result + (values != null ? values.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.sprintf("%s,%s,%s:: %s",
                name,
                interval,
                function,
                collectors != null ? collectors.toString() : "()"
        );
    }
}
