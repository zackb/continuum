package com.dlvr.continuum.series.db.impl;

import com.dlvr.continuum.io.file.impl.FileSystemReference;
import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.db.DB;
import com.dlvr.continuum.series.db.ID;
import com.dlvr.continuum.series.db.Iterator;
import com.dlvr.continuum.series.query.Query;
import com.dlvr.continuum.series.query.QueryResult;
import com.dlvr.continuum.util.Bytes;

import static com.dlvr.continuum.util.Bytes.*;

/**
 * DB implemented with RocksDB
 */
public class RockDB implements DB {

    private final RockSlab rock;

    private RockDB() throws Exception {
        throw new Exception();
    }

    public RockDB(RockSlab slab) {
        this.rock = slab;
    }

    public RockDB(String name, FileSystemReference baseRef) throws Exception {
        FileSystemReference dataDirRef = baseRef.getChild(name);
        this.rock = new RockSlab(name, dataDirRef);
    }

    @Override
    public void write(Datum datum) throws Exception {
        byte[] bytes = datum.ID().bytes();
        System.out.println("Write: " + Bytes.String(bytes));
        rock.put(datum.ID().bytes(), bytes(datum));
    }

    @Override
    public QueryResult query(Query query) {
        QueryResult result = null;
        Iterator itr = null;
        try {
            itr = iterator();
            itr.seekToFirst();
            while (itr.hasNext()) {
                ID id = itr.next();
                for (byte b : id.bytes()) {
                    System.out.printf("%02X ", b);
                }
            }
        } finally {
            if (itr != null) itr.close();
        }
        return result;
    }

    @Override
    public Iterator iterator() {
        return new RockIterator(rock);
    }

    @Override
    public void close() {
        rock.close();
    }

    public RockSlab getSlab() {
        return rock;
    }
}
