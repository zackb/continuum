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

    // underlying storage
    private final Slab rock;

    // dimension of this datastore TODO: column family??
    private final Dimension dimension;

    /**
     * Open or create a new slab
     * @param dimension for this data store // TODO: Dont need/want this? (column family)
     * @param slab underlying rock slab to use
     */
    public RockDB(Dimension dimension, Slab slab) {
        this.dimension = dimension;
        this.rock = slab;
    }

    /**
     * Open or create a new slab
     * @param dimension for this data store // TODO: Dont need/want this? (column family)
     * @param name unique id of slab
     * @param base file system reference to use
     * @throws Exception error opening data store
     */
    public RockDB(Dimension dimension, String name, FileSystemReference base) throws Exception {
        this.dimension = dimension;
        this.rock = new RockSlab(name + ".db", base);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open() throws Exception {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(Atom atom) throws Exception {
        rock.write(atom.ID().bytes(), Bytes.bytes(atom));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Atom read(AtomID id) throws Exception {
        return Bytes.Atom(rock.read(id.bytes()), dimension);
    }

    /**
     * TODO: Remove particles from body (just fields, tags are in the ID)
     * {@inheritDoc}
     */
    static Atom decodeAtom(byte[] bytes, Dimension dimension) {
        if (dimension == Dimension.TIME) {
            return Bytes.KAtom(bytes);
        } else if (dimension == Dimension.SPACE) {
            return Bytes.SAtom(bytes);
        }
        throw new ZiggyStardustError();
    }

    /**
     * TODO: Move out to Scanner class and just read Iterator from DB
     * {@inheritDoc}
     */
    @Override
    public Slice slice(Scan scan) {
        Iterator itr = null;
        Collector collector = Collectors.forSlice(scan);
        Filter filter = Filters.forSlice(scan, dimension); // TODO: filter fields and values
        try {
            itr = iterator();
            itr.seek(scan.ID().bytes());
            Atom atom = itr.valid() ? itr.get() : null;
            boolean stop = false;
            while (!stop && atom != null) {
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

        return collector.slice();
    }

    private Atom iterate(Iterator iterator) {
        iterator.next();
        if (!iterator.valid()) return null;
        return iterator.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator iterator() {
        return new RockIterator(dimension, slab());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
        rock.close();
    }

    /**
     * Get the underlying storage resource
     * @return the underlying storage slab for this DB
     */
    public RockSlab slab() {
        return (RockSlab)rock;
    }
}
