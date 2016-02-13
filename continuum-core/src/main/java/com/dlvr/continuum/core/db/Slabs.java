package com.dlvr.continuum.core.db;

import com.dlvr.continuum.db.AtomID;
import com.dlvr.continuum.db.Slab;
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
        // parse atom name to use as shard key
        AtomID id = Bytes.AtomID(key);
        return Math.abs(id.name().hashCode() % slabs.size());
    }

    private Slab select(byte[] key) {
        return slabs.get(hash(key));
    }

    @Override
    public byte[] get(byte[] key) throws Exception {
        return select(key).get(key);
    }

    @Override
    public void put(byte[] key, byte[] value) throws Exception {
        select(key).put(key, value);
    }

    @Override
    public void merge(byte[] key, byte[] value) throws Exception {
        select(key).merge(key, value);
    }

    @Override
    public void close() throws Exception {
        //slabs.forEach(Slab::close);
        for (Slab slab : slabs) {
            slab.close();
        }
    }
}
