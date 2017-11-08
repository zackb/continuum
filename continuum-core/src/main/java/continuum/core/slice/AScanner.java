package continuum.core.slice;

import continuum.atom.Atom;
import continuum.atom.AtomID;
import continuum.slab.Iterator;
import continuum.slice.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Base scanner using filters and collectors
 */
public class AScanner implements Scanner {

    private Iterator<Atom> iterator;

    private byte[] previousScan = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice slice(Scan scan) throws Exception {
        Collector collector = scan.collector();
        Filter filter = Filters.forScan(scan); // TODO: filter fields and values
        boolean decode = decodeBody(collector);
        byte[] start = previousScan == null ? scan.ID().bytes() : previousScan;
        iterator.seek(start);
        Atom atom = iterator.valid() ? iterator.get() : null;
        boolean stop = false;

        int totalRowsScanned = 0;
        int totalRowsCollected = 0;
        while (!stop && atom != null) {
            totalRowsScanned++;
            switch (filter.filter(atom)) {
                case CONTINUE:          if (decode) collect(collector);
                                        else collectID(collector);
                                        totalRowsCollected++;
                                        break;
                case SKIP:              break;
                case STOP: default:     stop = true;
                                        break;
            }

            if (scan.limit() != Const.LIMIT_NONE &&
                scan.limit() <= totalRowsCollected)
                break;


            atom = iterate(iterator);
            if (iterator.valid()) {
                previousScan = iterator.ID().bytes();
            }

        }

        //System.out.println("For Key: " + scan.name() + " Scanned: " + totalRowsScanned + " and Collected: " + totalRowsCollected);

        return collector.slice();
    }

    @Override
    public Stream<Slice> stream(Scan scan) throws Exception {
        return Stream.generate(AScanner.wrap(() -> slice(scan)));
    }


    public static <T> Supplier<T> wrap(Callable<T> callable) {
        return () -> {
            try {
                return callable.call();
            }
            catch (RuntimeException e) {
                throw e;
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void iterator(Iterator<Atom> iterator) {
        this.iterator = iterator;
    }

    private Atom iterate(Iterator<Atom> iterator) {
        iterator.next();
        if (!iterator.valid()) return null;
        return iterator.get();
    }

    private boolean decodeBody(Collector<?> collector) {
        Class<?> clazz = collector.getClass();
        Type[] types = clazz.getGenericInterfaces();
        Type type = ((ParameterizedType)types[0]).getActualTypeArguments()[0];

        if (Atom.class.getTypeName().equals(type.getTypeName()))
            return true;
        else if (AtomID.class.getTypeName().equals(type.getTypeName()))
            return false;
        throw new Error("Can not collect: " + type);
    }

    private void collect(Collector<Atom> collector) {
        Atom atom = iterator.get();
        collector.collect(atom);
    }

    private void collectID(Collector<AtomID> collector) {
        collector.collect(iterator.ID());
    }
}
