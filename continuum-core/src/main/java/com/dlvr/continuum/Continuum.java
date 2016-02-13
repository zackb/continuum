package com.dlvr.continuum;

import com.dlvr.continuum.core.atom.*;
import com.dlvr.continuum.core.db.RockSlab;
import com.dlvr.continuum.core.db.Slabs;
import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Fields;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.db.DB;
import com.dlvr.continuum.core.db.RockDB;
import com.dlvr.continuum.db.Slab;
import com.dlvr.continuum.db.query.Function;
import com.dlvr.continuum.db.query.Query;
import com.dlvr.continuum.db.query.QueryResult;
import com.dlvr.continuum.core.db.query.NQuery;
import com.dlvr.continuum.core.db.query.NQueryResult;
import com.dlvr.continuum.except.NoSuchDimensionError;
import com.dlvr.continuum.io.file.Reference;
import com.dlvr.continuum.util.Maths;
import com.dlvr.util.Metrics;

import java.io.Closeable;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.dlvr.continuum.util.Util.*;

/**
 * Root interface to library
 * Created by zack on 2/10/16.
 */
public class Continuum implements Closeable {

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

    public static AtomBuilder satom() {
        return new AtomBuilder().dimension(Dimension.SERIES);
    }

    public static AtomBuilder katom() {
        return new AtomBuilder().dimension(Dimension.KEYVALUE);
    }

    public static Particles particles(Map<String, String> particles) {
        return new NParticles(particles);
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
        for (int i = 0; i < bases.size(); i++) {
            Slab slab = new RockSlab(id + "." + i + ".db", bases.get(0));
            slabs.add(slab);
        }

        if (slabs.size() == 1) {
            this.db = new RockDB(dimension, slabs.get(0));
        } else {
            this.db = new RockDB(dimension, new Slabs(slabs));
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
    @Override
    public void close() {
        try {
            db.close();
        } catch (Exception e) {
            throw new Error(e);
        }
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

    /**
     * Compact atoms into quarks given a given time range and size
     * @param start
     * @param end
     * @param interval (retention policy)
     * @throws Exception
     */
    public void contract(long start, long end, TimeUnit interval) throws Exception() {

    }

    /**
     * Expand a continuum (universe)? from cold storage data
     */
    public void expand(Reference reference) {

    }

    public void delete(long start, long end) throws Exception {

    }

    public Reference clone(long start, long end) throws Exception {

    }

    public void

    // TODO: Dont even need this? atom.id knows what to do
    public static Builder series() {
        return new Builder().dimension(Dimension.SERIES);
    }

    public static Builder kv() {
        return new Builder().dimension(Dimension.KEYVALUE);
    }

    public static class Builder {
        private Dimension dimension = Dimension.SERIES;
        private List<FileSystemReference> base = new ArrayList<>();
        private String id = "continuum";
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
        private NParticles particles;
        private NFields fields;
        private long timestamp = System.currentTimeMillis();
        private double value;
        private Dimension dimension;
        private AtomBuilder() {}
        public AtomBuilder name(String name) {
            this.name = name;
            return this;
        }
        public AtomBuilder particles(Particles particles) {
            this.particles = (NParticles) particles;
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
        public AtomBuilder dimension(Dimension dimension) {
            this.dimension = dimension;
            return this;
        }
        public Atom build() {
            if (dimension == Dimension.SERIES)
                return new SAtom(name, particles, timestamp, fields, value);
            else if (dimension == Dimension.KEYVALUE)
                return new KAtom(name, particles, timestamp, fields, value);
            throw new NoSuchDimensionError();
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
        public QueryBuilder particles(Particles particles) {
            target.particles = particles;
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

    public enum Dimension {
        SERIES,KEYVALUE
    }

    private static Atom createAtom() {
        return Continuum.satom()
                .name("series" + Maths.randInt(0, 100))
                .value(Maths.randDouble(Double.MIN_VALUE, Double.MAX_VALUE))
                .timestamp(System.currentTimeMillis())
                .build();
    }

    // 50MB/10M metrics
    public static void main(String[] args) throws Exception {
        int iterations = 2000000000; //Integer.MAX_VALUE;
        FileSystemReference ref = new FileSystemReference("/tmp/LOAD");
        Builder b = series().base(ref);
        TimerTask timer = new MetricTimer().schedule(new Runnable() {
                                                           @Override
                                                           public void run() {
                                                               System.out.println(Metrics.report());
                                                           }
                                                       }, 5000);
        try (Continuum c = b.open()){
            final Continuum continuum = c;
            for (int i = 0; i < iterations; i++) {
                Metrics.time("write", () -> {
                    continuum.getDb().write(createAtom());
                    return null;
                });
            }
        } finally {
            //ref.delete();
            timer.cancel();
        }
    }

    static class MetricTimer {
        private final Timer t = new Timer();

        public TimerTask schedule(final Runnable r, long delay) {
            final TimerTask task = new TimerTask() { public void run() { r.run(); }};
            t.scheduleAtFixedRate(task, delay, delay);
            return task;
        }
    }
}
