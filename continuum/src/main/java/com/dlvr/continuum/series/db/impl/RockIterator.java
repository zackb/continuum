package com.dlvr.continuum.series.db.impl;

import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.db.DatumID;
import com.dlvr.continuum.series.db.Iterator;
import com.dlvr.continuum.util.Bytes;
import org.rocksdb.RocksIterator;

/**
 * Created by zack on 2/11/16.
 */
public class RockIterator implements Iterator {

    private RocksIterator it;

    private RockIterator() { }

    RockIterator(RockSlab slab) {
        this.it = slab.getRocksDB().newIterator();
    }

    @Override
    public Datum get() {
        return Bytes.Datum(it.value());
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
    public void seekToLast() {
        it.seekToLast();
    }

    @Override
    public boolean hasNext() {
        return it.isValid();
    }

    @Override
    public DatumID next() {
        it.next();
        return Bytes.DatumID(it.key());
    }

    @Override
    public void close() {
        if (it != null)
            it.dispose();
        it = null;
    }
}
