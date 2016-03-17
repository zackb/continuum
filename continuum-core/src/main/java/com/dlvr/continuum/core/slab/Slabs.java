package com.dlvr.continuum.core.slab;

import com.dlvr.continuum.atom.AtomID;
import com.dlvr.continuum.slab.Slab;
import com.dlvr.continuum.io.file.Reference;
import com.dlvr.continuum.util.Bytes;

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
    public void merge(byte[] key, byte[] value) throws Exception {
        select(key).merge(key, value);
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