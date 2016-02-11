package com.dlvr.continuum.series.db.impl;

import com.dlvr.continuum.io.file.impl.FileSystemReference;
import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.db.DB;
import com.dlvr.continuum.series.db.DatumID;
import com.dlvr.continuum.series.db.ID;
import com.dlvr.continuum.series.db.Iterator;
import com.dlvr.continuum.series.query.Query;
import com.dlvr.continuum.series.query.QueryResult;
import com.dlvr.util.BSON;

import static com.dlvr.continuum.util.Bytes.*;

/**
 * DB implemented with RocksDB
 */
public class RockDB implements DB {

    private static final byte b = 0x0;

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
        rock.put(datum.ID().bytes(), bytes(datum));
    }

    /**
     * Encode datum as bytes[bson] + 0x0 + bytes[value]
     * @param datum to encode body
     * @return body
     */
    private static byte[] body(Datum datum) {
        byte[] bson = BSON.encode(datum);
        byte[] value = bytes(datum.value());
        return null;
    }

    @Override
    public QueryResult query(Query query) {
        QueryResult result = null;
        Iterator itr = null;
        try {
            itr = iterator();
            itr.seekToFirst();
            while (itr.hasNext()) {
                DatumID id = itr.next();
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
