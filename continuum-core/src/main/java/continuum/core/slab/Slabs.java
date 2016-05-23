package continuum.core.slab;

import continuum.atom.AtomID;
import continuum.slab.Slab;
import continuum.file.Reference;
import continuum.util.Bytes;

import java.util.List;

/**
 * Slabs Sharded on disk
 * Created by zack on 2/12/16.
 */
public class Slabs implements Slab {

    private final List<Slab> slabs;

    public Slabs(List<Slab> slabs) {
        this.slabs = slabs;
    }

    private int hash(byte[] key) {
        // TODO: SPACE instead
        // parse atom name to use as shard key
        AtomID id = Bytes.SAtomID(key);
        return Math.abs(id.name().hashCode() % slabs.size());
    }

    private Slab select(byte[] key) {
        return slabs.get(hash(key));
    }

    @Override
    public void open() throws Exception {

    }

    @Override
    public byte[] read(byte[] key) throws Exception {
        return select(key).read(key);
    }

    @Override
    public void write(byte[] key, byte[] value) throws Exception {
        select(key).write(key, value);
    }

    @Override
    public void delete(byte[] key) throws Exception {
        select(key).delete(key);
    }

    @Override
    public void merge(byte[] key, byte[] value) throws Exception {
        select(key).merge(key, value);
    }

    @Override
    public Long count() throws Exception {
        long count = 0L;
        for (Slab slab : slabs) count += slab.count();
        return count;
    }

    @Override
    public Reference reference() {
        throw new Error("Not implemented: Sharded slab references. Tee??");
    }

    @Override
    public void close() throws Exception {
        //slabs.forEach(Slab::close);
        for (Slab slab : slabs) {
            slab.close();
        }
    }

    public Slab get(int idx) {
        return slabs.get(idx);
    }

    public int size() {
        return slabs.size();
    }
}
