package com.dlvr.continuum.core.slice;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.AtomID;
import com.dlvr.continuum.slab.Iterator;
import com.dlvr.continuum.slice.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Base scanner using filters and collectors
 */
public class NScanner implements Scanner {

    private Iterator<Atom> iterator;

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice slice(Scan scan) throws Exception {
        Collector collector = Collectors.forSlice(scan);
        Filter filter = Filters.forScan(scan); // TODO: filter fields and values
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

    private void collectAtom(Collector<Atom> collector) {
        collector.collect(iterator.get());
    }

    private void collectID(Collector<AtomID> collector) {
        collector.collect(iterator.ID());
    }
}
