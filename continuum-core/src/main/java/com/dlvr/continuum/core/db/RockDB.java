package com.dlvr.continuum.core.db;

import com.dlvr.continuum.core.db.scan.Filters;
import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.DB;
import com.dlvr.continuum.db.AtomID;
import com.dlvr.continuum.db.Iterator;
import com.dlvr.continuum.db.Slab;
import com.dlvr.continuum.db.scan.Filter;
import com.dlvr.continuum.db.scan.Scan;
import com.dlvr.continuum.db.scan.ScanResult;
import com.dlvr.continuum.core.db.scan.Collectors;
import com.dlvr.continuum.db.scan.Collector;
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
    private final Dimension dimension;

    public RockDB(Dimension dimension, Slab slab) {
        this.dimension = dimension;
        this.rock = slab;
    }

    public RockDB(Dimension dimension, String name, FileSystemReference base) throws Exception {
        this.dimension = dimension;
        rock = new RockSlab(name + ".db", base);
    }

    @Override
    public void write(Atom atom) throws Exception {
        rock.put(atom.ID().bytes(), value(atom));
    }

    public Atom get(AtomID id) throws Exception {
        return decodeAtom(rock.get(id.bytes()));
    }

    /**
     * // TODO: Store stats instead of value/avg?
     *      Quark! byte[bson(fields)] + 0x0 + byte[min] + 0x0 + byte[max] + 0x0 + byte[sum] + byte[count] + 0x0
     * // TOOD: String value support
     *
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
     * TODO: Remove particles from body (just fields, tags are in the id)
     * Decode atom from slab storage
     * @param bytes encoded with {#value(Atom)}
     * @return
     */
    static Atom decodeAtom(byte[] bytes) {
        // TODO KATOM!
        return Bytes.SAtom(bytes);
    }

    /**
     * TODO: min/max/sum/count instead of value?
     *      Quark!
     * TODO: String values
     * Decode only the value of the atom. If the full body is not needed to be decoded.
     * @param bytes encoded with {#value(Atom)}
     * @return
     */
    static double decodeValue(byte[] bytes) {
        return Double(range(bytes, bytes.length - 8, bytes.length));
    }

    @Override
    public ScanResult scan(Scan scan) {
        Iterator itr = null;
        Collector collector = Collectors.forScan(scan);
        Filter filter = Filters.forScan(scan);
        try {
            itr = iterator();
            itr.seek(scan.ID().bytes());
            do {
                collect(collector, scan, itr);
                filter(filter, scan, itr);
            } while (itr.next());
        } finally {
            if (itr != null) itr.close();
        }

        return result(scan.function().name())
                .value(collector.value())
                .build();
    }

    private void collect(Collector collector, Scan scan, Iterator iterator) {
        collector.collect(iterator);
    }

    private void filter(Filter filter, Scan scan, Iterator iterator) {
        if (filter == null) return;
        filter.filter(iterator);
    }

    @Override
    public Iterator iterator() {
        return new RockIterator(dimension, (RockSlab)rock);
    }

    @Override
    public void close() throws Exception {
        rock.close();
    }

    public RockSlab getSlab() {
        return (RockSlab)rock;
    }
}
