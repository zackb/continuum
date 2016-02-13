package com.dlvr.continuum;

import com.dlvr.continuum.core.db.RockSlab;
import com.dlvr.continuum.core.db.Slabs;
import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Fields;
import com.dlvr.continuum.atom.Tags;
import com.dlvr.continuum.core.atom.NAtom;
import com.dlvr.continuum.core.atom.NFields;
import com.dlvr.continuum.core.atom.NTags;
import com.dlvr.continuum.db.DB;
import com.dlvr.continuum.core.db.RockDB;
import com.dlvr.continuum.db.Slab;
import com.dlvr.continuum.db.query.Function;
import com.dlvr.continuum.db.query.Query;
import com.dlvr.continuum.db.query.QueryResult;
import com.dlvr.continuum.core.db.query.NQuery;
import com.dlvr.continuum.core.db.query.NQueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.dlvr.continuum.util.Util.*;

/**
 * Root interface to library
 * Created by zack on 2/10/16.
 */
public class Continuum {

    private final String id;

    private final Dimension dimension;

    private final DB db;

    private final List<FileSystemReference> bases;

    public static void sayHello() {
        System.out.printf("People of Earth, how are you?\n");
    }

    public static AtomBuilder atom() {
        return new AtomBuilder();
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

    private Continuum(String id, Dimension dimension, List<FileSystemReference> bases) throws Exception {
        checkNotNull("id", id);
        checkNotNull("dimension", dimension);
        checkNotNull("base", bases);
        this.id = id;
        this.dimension = dimension;
        this.bases = bases;

        List<Slab> slabs = new ArrayList<>();
        for (int i = 0; i < slabs.size(); i++) {
            Slab slab = new RockSlab(id + "." + i + ".db", bases.get(0));
            slabs.add(slab);
        }

        if (slabs.size() == 1) {
            this.db = new RockDB(slabs.get(0));
        } else {
            this.db = new RockDB(new Slabs(slabs));
        }
    }

    /**
     * Access the backing datastore for this continuum
     * @return DB datastore engine
     */
    public DB getDb() {
        return this.db;
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
        //bases.forEach(FileSystemReference::delete);
        for (FileSystemReference reference : bases) {
            reference.delete();
        }
    }

    public static Builder continuum() {
        return new Builder();
    }

    public static Builder series() {
        return new Builder().dimension(Dimension.SERIES);
    }

    public static Builder kv() {
        return new Builder().dimension(Dimension.KEYVALUE);
    }

    public static class Builder {
        private Dimension dimension = Dimension.SERIES;
        private List<FileSystemReference> base = new ArrayList<>();
        private String id = "default";
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        public Builder base(FileSystemReference base) {
            this.base.add(base);
            return this;
        }
        public Builder base(FileSystemReference... bases) {
            for (FileSystemReference ref : bases) {
                this.base.add(ref);
            }
            return this;
        }
        public Builder dimension(Dimension dimension) {
            this.dimension = dimension;
            return this;
        }
        public Continuum open() throws Exception {
            return new Continuum(id, dimension, base);
        }
    }

    public static class AtomBuilder {

        private String name;
        private NTags tags;
        private NFields fields;
        private long timestamp;
        private double value;
        private AtomBuilder() {}
        public AtomBuilder name(String name) {
            this.name = name;
            return this;
        }
        public AtomBuilder tags(Tags tags) {
            this.tags = (NTags)tags;
            return this;
        }
        public AtomBuilder fields(Fields fields) {
            this.fields = (NFields)fields;
            return this;
        }
        public AtomBuilder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        public AtomBuilder value(double value) {
            this.value = value;
            return this;
        }
        public Atom build() {
            return new NAtom(name, tags, timestamp, fields, value);
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

    private enum Dimension {
        SERIES,KEYVALUE
    }
}
