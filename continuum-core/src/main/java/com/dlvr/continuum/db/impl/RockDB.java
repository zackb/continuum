package com.dlvr.continuum.db.impl;

import com.dlvr.continuum.io.file.impl.FileSystemReference;
import com.dlvr.continuum.db.datum.Datum;
import com.dlvr.continuum.db.DB;
import com.dlvr.continuum.db.DatumID;
import com.dlvr.continuum.db.Iterator;
import com.dlvr.continuum.db.query.Query;
import com.dlvr.continuum.db.query.QueryResult;
import com.dlvr.continuum.db.query.impl.Collector;
import com.dlvr.continuum.db.query.impl.ICollector;
import com.dlvr.util.BSON;

import static com.dlvr.continuum.util.Bytes.Double;
import static com.dlvr.continuum.util.Bytes.*;
import static com.dlvr.continuum.Continuum.*;

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
        rock.put(datum.ID().bytes(), value(datum));
    }

    // TODO: Remove tags from body (its in the id)
    public Datum get(DatumID id) throws Exception {
        return decodeDatum(rock.get(id.bytes()));
    }

    /**
     * Encode datum as bytes[bson] + 0x0 + bytes[value]
     * @param datum to encode
     * @return value for slab storage
     */
    static byte[] value(Datum datum) {
        byte[] bson = BSON.encode(datum);
        byte[] value = bytes(datum.value());
        byte[] result = new byte[bson.length + 1 + value.length];
        append(result, 0, bson);
        append(result, bson.length, b);
        append(result, bson.length + 1, value);
        return result;
    }

    /**
     * Decode datum from slab storage
     * @param bytes encoded with {#value(Datum)}
     * @return
     */
    static Datum decodeDatum(byte[] bytes) {
        return null;
    }

    /**
     * Decode only the value of the datum. If the full body is not needed to be decoded.
     * @param bytes encoded with {#value(Datum)}
     * @return
     */
    static double decodeValue(byte[] bytes) {
        return Double(range(bytes, bytes.length - 8, bytes.length));
    }

    @Override
    public QueryResult query(Query query) {
        Iterator itr = null;
        ICollector collector = Collector.forQuery(query);
        try {
            itr = iterator();
            itr.seekToFirst();
            do {
                collect(collector, itr);
            } while (itr.next());
        } finally {
            if (itr != null) itr.close();
        }

        return result(query.function().name())
                .value(collector.value())
                .build();
    }

    private void collect(ICollector collector, Iterator iterator) {
        collector.collect(iterator);
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
