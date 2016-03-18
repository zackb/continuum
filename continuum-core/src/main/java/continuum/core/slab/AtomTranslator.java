package continuum.core.slab;

import continuum.atom.Atom;
import continuum.atom.AtomID;
import continuum.slab.Iterator;
import continuum.slab.Slab;
import continuum.slab.Translator;
import continuum.except.ZiggyStardustError;
import continuum.util.Bytes;

import static continuum.Continuum.*;

/**
 * Atom data store, uses RockSlab
 */
public class AtomTranslator implements Translator<Atom> {

    // underlying storage
    private final Slab slab;

    // dimension of this datastore TODO: column family??
    private final Dimension dimension;

    /**
     * Open or create a new slab
     * @param dimension for this data store // TODO: Dont need/want this? (column family)
     * @param slab underlying slab to use
     */
    public AtomTranslator(Dimension dimension, Slab slab) {
        this.dimension = dimension;
        this.slab = slab;
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
    public Iterator<Atom> iterator() {
        return new AtomIterator(dimension, (RockSlab)slab());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Atom> iterator(boolean streaming) {
        return new TailingIterator(dimension, (RockSlab)slab());
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
    public Slab slab() {
        return slab;
    }
}
