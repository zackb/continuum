package com.dlvr.continuum.core.db.slice;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.AtomID;
import com.dlvr.continuum.db.Iterator;
import com.dlvr.continuum.db.slice.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Base scanner using filters and collectors
 */
public class NScanner implements Scanner {

    private Iterator iterator;
    private Continuum.Dimension dimension;

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice slice(Scan scan) throws Exception {
        Collector collector = Collectors.forSlice(scan);
        Filter filter = Filters.forSlice(scan, dimension); // TODO: filter fields and values
        boolean decode = decodeBody(collector);
        iterator.seek(scan.ID().bytes());
        Atom atom = iterator.valid() ? iterator.get() : null;
        boolean stop = false;
        while (!stop && atom != null) {
            switch (filter.filter(atom)) {
                case CONTINUE:          if (decode) collectAtom(collector);
                                        else collectID(collector);
                                        break;
                case SKIP:              break;
                case STOP: default:     stop = true;
                                        break;
            }
            atom = iterate(iterator);
        }

        return collector.slice();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator iterator() {
        return iterator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void iterator(Iterator iterator) {
        this.iterator = iterator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dimension(Continuum.Dimension dimension) {
        this.dimension = dimension;
    }

    private Atom iterate(Iterator iterator) {
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

    private void collectAtom(Collector<Atom> collector) {
        collector.collect(iterator.get());
    }

    private void collectID(Collector<AtomID> collector) {
        collector.collect(iterator.ID());
    }
}
