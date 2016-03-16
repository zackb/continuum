package com.dlvr.continuum.core.db;

import com.dlvr.continuum.core.db.slice.Filters;
import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.*;
import com.dlvr.continuum.db.slice.Filter;
import com.dlvr.continuum.db.slice.Scan;
import com.dlvr.continuum.db.slice.Slice;
import com.dlvr.continuum.core.db.slice.Collectors;
import com.dlvr.continuum.db.slice.Collector;
import com.dlvr.continuum.except.ZiggyStardustError;
import com.dlvr.continuum.util.Bytes;
import static com.dlvr.continuum.Continuum.*;

/**
 * rockdDB implemented with RocksDB
 */
public class RockDB implements DB {

    private final Slab rock;
    private final Dimension dimension;

    public RockDB(Dimension dimension, Slab slab) {
        this.dimension = dimension;
        this.rock = slab;
    }

    public RockDB(Dimension dimension, String name, FileSystemReference base) throws Exception {
        this.dimension = dimension;
        rock = new RockSlab(name + ".db", base);
    }

    @Override
    public void write(Atom atom) throws Exception {
        rock.put(atom.ID().bytes(), Bytes.bytes(atom));
    }

    public Atom get(AtomID id) throws Exception {
        return Bytes.Atom(rock.get(id.bytes()), dimension);
    }

    /**
     * TODO: Remove particles from body (just fields, tags are in the id)
     * Decode atom from slab storage
     * @param bytes encoded with {#values(Atom)}
     * @return
     */
    static Atom decodeAtom(byte[] bytes, Dimension dimension) {
        if (dimension == Dimension.TIME) {
            return Bytes.KAtom(bytes);
        } else if (dimension == Dimension.SPACE) {
            return Bytes.SAtom(bytes);
        }
        throw new ZiggyStardustError();
    }

    @Override
    public Slice slice(Scan scan) {
        Iterator itr = null;
        Collector collector = Collectors.forSlice(scan);
        Filter filter = Filters.forSlice(scan, dimension); // TODO: filter fields and values
        try {
            itr = iterator();
            itr.seek(scan.ID().bytes());
            Atom atom = itr.hasNext() ? itr.get() : null;
            boolean stop = false;
            while (atom != null && !stop) {
                switch (filter.filter(atom)) {
                    case CONTINUE:          collector.collect(atom); break;
                    case SKIP:              break;
                    case STOP: default:     stop = true; break;
                }
                atom = iterate(itr);
            }
        } finally {
            if (itr != null) itr.close();
        }

        return collector.result();
    }

    private Atom iterate(Iterator iterator) {
        if (!iterator.hasNext()) return null;
        if (dimension == Dimension.TIME)
             iterator.prev();
        else iterator.next();
        if (!iterator.hasNext()) return null;
        return iterator.get();
    }

    @Override
    public Iterator iterator() {
        return new RockIterator(dimension, slab());
    }

    @Override
    public void close() throws Exception {
        rock.close();
    }

    public RockSlab slab() {
        return (RockSlab)rock;
    }
}
