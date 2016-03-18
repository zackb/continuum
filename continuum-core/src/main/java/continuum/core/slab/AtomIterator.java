package continuum.core.slab;

import continuum.Continuum;
import continuum.atom.Atom;
import continuum.atom.Values;
import continuum.atom.AtomID;
import continuum.slab.Iterator;
import continuum.except.ZiggyStardustError;
import continuum.util.Bytes;
import org.rocksdb.RocksIterator;

/**
 * Atom translator. Turn slab storage bytes into data objects
 * Created by zack on 2/11/16.
 */
public class AtomIterator implements Iterator<Atom> {

    private final Continuum.Dimension dimension;
    private final RocksIterator it;

    AtomIterator(Continuum.Dimension dimension, RockSlab slab) {
        this.dimension = dimension;
        this.it = slab.rockdDB().newIterator();
    }

    @Override
    public AtomID ID() {
        if (dimension == Continuum.Dimension.SPACE)
            return Bytes.SAtomID(it.key());
        else if (dimension == Continuum.Dimension.TIME)
            return Bytes.KAtomID(it.key());
        throw new ZiggyStardustError();
    }

    @Override
    public Atom get() {
        return AtomTranslator.decodeAtom(it.value(), dimension);
    }

    @Override
    public Values values() {
        // TODO: Lazy decode?
        return Bytes.Atom(it.value(), dimension).values();
    }

    @Override
    public void seekToFirst() {
        it.seekToFirst();
    }

    @Override
    public void seek(byte[] pos) {
        it.seek(pos);
    }

    @Override
    public boolean valid() {
        return it.isValid();
    }

    @Override
    public boolean next() {
        it.next();
        return valid();
    }

    @Override
    public void close() {
        if (it != null)
            it.dispose();
    }
}
