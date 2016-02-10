package com.dlvr.continuum.series.db.impl;

import com.dlvr.continuum.io.file.impl.FileSystemReference;
import com.dlvr.continuum.series.db.SeriesDB;
import com.dlvr.continuum.series.point.Point;
import com.dlvr.continuum.series.query.Query;
import com.dlvr.continuum.series.query.QueryResult;
import com.dlvr.continuum.util.Bytes;

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
    public void write(Point point) throws Exception {
        rock.merge(key(point), Bytes.bytes(point.value()));
    }

    @Override
    public QueryResult query(Query query) {
        return null;
    }

    private static String key(Point point) {
        String key = point.name();
        for (String tag : point.tags().names()) {
            key += ':' + point.tags().get(tag);
        }
        key += ':' + point.timestamp();
        return key;
    }

    public void close() {
        rock.close();
    }
}
