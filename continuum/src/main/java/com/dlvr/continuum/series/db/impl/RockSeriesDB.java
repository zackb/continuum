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
public class RockSeriesDB implements SeriesDB {

    private final FileSystemReference baseRef;

    private final RockSlab rock;

    public RockSeriesDB(String name, FileSystemReference baseRef) throws Exception {
        this.baseRef = baseRef;
        this.rock = new RockSlab(name, baseRef.getFullPath());
    }

    @Override
    public void write(Datum datum) throws Exception {
        rock.put(bytes(datum.ID()), bytes(datum));
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
