package com.dlvr.continuum.core.db;

import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.*;
import com.dlvr.continuum.except.ZiggyStardustError;
import com.dlvr.continuum.util.Bytes;
import static com.dlvr.continuum.Continuum.*;

/**
 * Atom data store, uses RockSlab
 */
public class AtomDB implements DB {

    // underlying storage
    private final Slab slab;

    // dimension of this datastore TODO: column family??
    private final Dimension dimension;

    /**
     * Open or create a new slab
     * @param dimension for this data store // TODO: Dont need/want this? (column family)
     * @param slab underlying slab to use
     */
    public AtomDB(Dimension dimension, Slab slab) {
        this.dimension = dimension;
        this.slab = slab;
    }

    /**
     * Open or create a new slab
     * @param dimension for this data store // TODO: Dont need/want this? (column family)
     * @param name unique id of slab
     * @param base file system reference to use
     * @throws Exception error opening data store
     */
    public AtomDB(Dimension dimension, String name, FileSystemReference base) throws Exception {
        this.dimension = dimension;
        this.slab = new RockSlab(name + ".db", base);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open() throws Exception {
        this.slab.open();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(Atom atom) throws Exception {
        slab.write(atom.ID().bytes(), Bytes.bytes(atom));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Atom read(AtomID id) throws Exception {
        return Bytes.Atom(slab.read(id.bytes()), dimension);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator iterator() {
        return new RockIterator(dimension, (RockSlab)slab());
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
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
        slab.close();
    }

    /**
     * Get the underlying storage resource
     * @return the underlying storage slab for this DB
     */
    public Slab slab() {
        return slab;
    }
}
