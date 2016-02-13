package com.dlvr.continuum.core.db;

import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.DB;
import com.dlvr.continuum.db.AtomID;
import com.dlvr.continuum.db.Iterator;
import com.dlvr.continuum.db.Slab;
import com.dlvr.continuum.db.query.Query;
import com.dlvr.continuum.db.query.QueryResult;
import com.dlvr.continuum.core.db.query.Collectors;
import com.dlvr.continuum.db.query.Collector;
import com.dlvr.continuum.util.Bytes;
import com.dlvr.util.BSON;

import static com.dlvr.continuum.util.Bytes.Double;
import static com.dlvr.continuum.util.Bytes.*;
import static com.dlvr.continuum.Continuum.*;

/**
 * DB implemented with RocksDB
 */
public class RockDB implements DB {

    private static final byte b = 0x0;

    private final Slab rock;

    public RockDB(Slab slab) {
        this.rock = slab;
    }

    public RockDB(String name, FileSystemReference base) throws Exception {
        rock = new RockSlab(name + ".db", base);
    }

    @Override
    public void write(Atom atom) throws Exception {
        rock.put(atom.ID().bytes(), value(atom));
    }

    // TODO: Remove tags from body (its in the id)
    public Atom get(AtomID id) throws Exception {
        return decodeAtom(rock.get(id.bytes()));
    }

    /**
     * Encode atom as bytes[bson] + 0x0 + bytes[value]
     * @param atom to encode
     * @return value for slab storage
     */
    static byte[] value(Atom atom) {
        byte[] bson = BSON.encode(atom);
        byte[] value = bytes(atom.value());
        byte[] result = new byte[bson.length + 1 + value.length];
        append(result, 0, bson);
        append(result, bson.length, b);
        append(result, bson.length + 1, value);
        return result;
    }

    /**
     * Decode atom from slab storage
     * @param bytes encoded with {#value(Atom)}
     * @return
     */
    static Atom decodeAtom(byte[] bytes) {
        return Bytes.Atom(bytes);
    }

    /**
     * Decode only the value of the atom. If the full body is not needed to be decoded.
     * @param bytes encoded with {#value(Atom)}
     * @return
     */
    static double decodeValue(byte[] bytes) {
        return Double(range(bytes, bytes.length - 8, bytes.length));
    }

    @Override
    public QueryResult query(Query query) {
        Iterator itr = null;
        Collector collector = Collectors.forQuery(query);
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

    private void collect(Collector collector, Iterator iterator) {
        collector.collect(iterator);
    }

    @Override
    public Iterator iterator() {
        return new RockIterator((RockSlab)rock);
    }

    @Override
    public void close() throws Exception {
        rock.close();
    }

    public RockSlab getSlab() {
        return (RockSlab)rock;
    }
}
