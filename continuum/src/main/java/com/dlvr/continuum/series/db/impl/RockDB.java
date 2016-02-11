package com.dlvr.continuum.series.db.impl;

import com.dlvr.continuum.io.file.impl.FileSystemReference;
import com.dlvr.continuum.series.db.SeriesDB;
import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.query.Query;
import com.dlvr.continuum.series.query.QueryResult;

import static com.dlvr.continuum.util.Bytes.*;

/**
 * SeriesDB implemented with RocksDB
 */
public class RockDB implements SeriesDB {

    private final RockSlab rock;

    private RockDB() throws Exception {
        throw new Exception();
    }

    public RockDB(RockSlab slab) {
        this.rock = slab;
    }

    public RockDB(String name, FileSystemReference baseRef) throws Exception {
        this.rock = new RockSlab(name, baseRef.getFullPath());
    }

    @Override
    public void write(Datum datum) throws Exception {
        rock.put(datum.ID().bytes(), bytes(datum));
    }

    @Override
    public QueryResult query(Query query) {
        return null;
    }

    @Override
    public void close() {
        rock.close();
    }
}
