package com.dlvr.continuum;

import com.dlvr.continuum.io.file.impl.FileSystemReference;
import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.datum.Fields;
import com.dlvr.continuum.series.datum.Tags;
import com.dlvr.continuum.series.datum.impl.NDatum;
import com.dlvr.continuum.series.datum.impl.NFields;
import com.dlvr.continuum.series.datum.impl.NTags;
import com.dlvr.continuum.series.db.DB;
import com.dlvr.continuum.series.db.impl.RockDB;
import com.dlvr.continuum.series.query.Function;
import com.dlvr.continuum.series.query.Query;
import com.dlvr.continuum.series.query.QueryResult;
import com.dlvr.continuum.series.query.impl.NQuery;
import com.dlvr.continuum.series.query.impl.NQueryResult;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Root interface to library
 * Created by zack on 2/10/16.
 */
public class Continuum {

    private final String id;

    private final StorageType type;

    private final DB db;

    private final FileSystemReference base;

    public static void sayHello() {
        System.out.printf("People of Earth, how are you?\n");
    }

    public static DatumBuilder datum(String name) {
        return new DatumBuilder(name);
    }

    public static Tags tags(Map<String, String> tags) {
        return new NTags(tags);
    }

    public static Fields fields(Map<String, Object> fields) {
        return new NFields(fields);
    }

    public static QueryBuilder query(String name) {
        return new QueryBuilder(name);
    }

    public static QueryResultBuilder result(String name) {
        return new QueryResultBuilder(name);
    }

    private Continuum(String id, StorageType type, FileSystemReference base) throws Exception {
        checkNotNull("id", id);
        checkNotNull("type", type);
        checkNotNull("base", base);
        this.id = id;
        this.type = type;
        this.base = base;
        this.db = new RockDB(id, base);
    }

    /**
     * Close all underlying resources for this continuum
     * @throws Exception on failure
     */
    public void close() throws Exception {
        db.close();
    }

    /**
     * CAUTION: Deletes all fileystem resources associated with this continuum
     * @throws Exception on failure
     */
    public void delete() throws Exception {
        db.close();
        base.delete();
    }

    public static Builder continuum() {
        return new Builder();
    }

    public static Builder series() {
        return new Builder().type(StorageType.SERIES);
    }

    public static Builder kv() {
        return new Builder().type(StorageType.KEYVALUE);
    }

    public static class Builder {
        private StorageType type = StorageType.SERIES;
        private FileSystemReference base;
        private String id = "default";
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        public Builder base(FileSystemReference base) {
            this.base = base;
            return this;
        }
        public Builder type(StorageType type) {
            this.type = type;
            return this;
        }
        public Continuum open() throws Exception {
            return new Continuum(id, type, base);
        }
    }

    public static class DatumBuilder {

        private final NDatum target = new NDatum();

        private DatumBuilder() {}
        DatumBuilder(String name) {
            target.name = name;
        }
        public DatumBuilder tags(Tags tags) {
            target.tags = (NTags)tags;
            return this;
        }
        public DatumBuilder fields(Fields fields) {
            target.fields = (NFields)fields;
            return this;
        }
        public DatumBuilder timestamp(long timestamp) {
            target.timestamp = timestamp;
            return this;
        }
        public DatumBuilder value(double value) {
            target.value = value;
            return this;
        }
        public Datum build() {
            return target;
        }
    }

    public static class QueryBuilder {
        private final NQuery target = new NQuery();
        private QueryBuilder() {}
        QueryBuilder(String name) {
            target.name = name;
        }
        public QueryBuilder start(long start) {
            target.start = start;
            return this;
        }
        public QueryBuilder end(long end) {
            target.end = end;
            return this;
        }
        public QueryBuilder interval(TimeUnit interval) {
            target.interval = interval;
            return this;
        }
        public QueryBuilder function(Function function) {
            target.function = function;
            return this;
        }
        public QueryBuilder tags(Tags tags) {
            target.tags = tags;
            return this;
        }
        public QueryBuilder fields(Fields fields) {
            target.fields = fields;
            return this;
        }

        public Query build() {
            return target;
        }
    }

    public static class QueryResultBuilder {
        private final NQueryResult target = new NQueryResult();
        private QueryResultBuilder() { }
        QueryResultBuilder(String name) {
            target.name = name;
        }
        public QueryResultBuilder value(double value) {
            target.value = value;
            return this;
        }

        public QueryResult build() {
            return target;
        }
    }

    private enum StorageType {
        SERIES,KEYVALUE
    }

    private void checkNotNull(String name, Object value) throws NullPointerException {
        if (value == null) {
            throw new NullPointerException("Option: '" + name + "'must be set");
        }
    }
}
