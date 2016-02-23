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
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.Slice;
import com.dlvr.continuum.db.slice.SliceResult;
import com.dlvr.continuum.core.db.slice.NSlice;
import com.dlvr.continuum.core.db.slice.NSliceResult;
import com.dlvr.continuum.except.ZiggyStardustError;
import com.dlvr.continuum.io.file.Reference;
import com.dlvr.continuum.util.Maths;
import com.dlvr.datetime.Interval;
import com.dlvr.util.Metrics;

import java.io.Closeable;
import java.util.*;

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

    public AtomBuilder atom() {
        return new AtomBuilder().dimension(dimension);
    }

    public static AtomBuilder satom() {
        return new AtomBuilder().dimension(Dimension.SPACE);
    }

    public static AtomBuilder katom() {
        return new AtomBuilder().dimension(Dimension.TIME);
    }

    public static Particles particles(Map<String, String> particles) {
        return new NParticles(particles);
    }

    public static Fields fields(Map<String, Object> fields) {
        return new NFields(fields);
    }

    public static SliceBuilder slice(String name) {
        return new SliceBuilder(name);
    }

    public static SliceResultBuilder result(String name) {
        return new SliceResultBuilder(name);
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
    public DB db() {
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
     * @param slice time slice to compact
     * @throws Exception
     */
    public void contract(Slice slice) throws Exception {

    }

    /**
     * Expand a continuum (universe)? from cold storage data
     */
    public void expand(Reference reference) throws Exception {

    }

    public void delete(Slice slice) throws Exception {

    }

    public Reference clone(Slice slice) throws Exception {
        return null;
    }

    // TODO: Dont even need this? atom.id knows what to do
    public static Builder series() {
        return new Builder().dimension(Dimension.TIME);
    }

    public static Builder kv() {
        return new Builder().dimension(Dimension.SPACE);
    }

    public static class Builder {
        private Dimension dimension = Dimension.TIME;
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
        public AtomBuilder particles(Map<String, String> particles) {
            //target.particles = particles;
            throw new Error("TODO");
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
            if (dimension == Dimension.SPACE)
                return new SAtom(name, particles, timestamp, fields, value);
            else if (dimension == Dimension.TIME)
                return new KAtom(name, particles, timestamp, fields, value);
            throw new ZiggyStardustError();
        }
    }

    public static class SliceBuilder {
        private final NSlice target = new NSlice();
        private SliceBuilder() {}
        SliceBuilder(String name) {
            target.name = name;
        }
        public SliceBuilder start(long start) {
            target.start = start;
            return this;
        }
        public SliceBuilder end(long end) {
            target.end = end;
            return this;
        }
        public SliceBuilder interval(Interval interval) {
            target.interval = interval;
            return this;
        }
        public SliceBuilder function(Function function) {
            target.function = function;
            return this;
        }
        public SliceBuilder particles(Map<String, String> particles) {
            //target.particles = particles;
            throw new Error("TODO");
        }
        public SliceBuilder particles(Particles particles) {
            target.particles = particles;
            return this;
        }
        public SliceBuilder fields(Fields fields) {
            target.fields = fields;
            return this;
        }

        public Slice build() {
            return target;
        }
    }

    public static class SliceResultBuilder {
        private final NSliceResult target = new NSliceResult();
        private SliceResultBuilder() { }
        SliceResultBuilder(String name) {
            target.name = name;
        }
        public SliceResultBuilder value(Double value) {
            target.value = value;
            return this;
        }
        public SliceResultBuilder timestamp(long timestamp) {
            target.timestamp = timestamp;
            return this;
        }

        public SliceResult build() {
            return target;
        }
    }

    public enum Dimension {
        SPACE, TIME
    }

    static class LoadTest {

        private final Continuum continuum;

        LoadTest(Continuum continuum) {
            this.continuum = continuum;
        }

        private final TimerTask timer = new MetricTimer().schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println(Metrics.report());
            }
        }, 5000);

        private static Atom createAtom() {
            return Continuum.satom()
                    .name("series" + Maths.randInt(0, 100))
                    .value(Maths.randDouble(Double.MIN_VALUE, Double.MAX_VALUE))
                    .timestamp(System.currentTimeMillis())
                    .build();
        }

        // 50MB/10M metrics
        public static void main(String[] args) throws Exception {
            FileSystemReference ref = new FileSystemReference("/tmp/LOAD");
            Builder b = series().base(ref);
            LoadTest test = null;
            try (Continuum c = b.open()){
                test = new LoadTest(c);
                test.load(c);
            } finally {
                if (test != null) test.stop();
                //ref.delete();
            }
        }

        private void stop() {
            timer.cancel();
        }

        private void load(final Continuum continuum) {
            int iterations = 2000000000; //Integer.MAX_VALUE;
            for (int i = 0; i < iterations; i++) {
                Metrics.time("write", () -> {
                    continuum.db().write(createAtom());
                    return null;
                });
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
}
