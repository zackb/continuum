package com.dlvr.continuum;

import com.dlvr.continuum.atom.Values;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Fields;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.core.db.slice.NScanner;
import com.dlvr.continuum.db.AtomID;
import com.dlvr.continuum.db.DB;
import com.dlvr.continuum.db.Slab;
import com.dlvr.continuum.db.slice.*;

import com.dlvr.continuum.core.atom.*;
import com.dlvr.continuum.core.db.slice.NSlice;
import com.dlvr.continuum.core.db.Slabs;
import com.dlvr.continuum.core.db.RockSlab;
import com.dlvr.continuum.core.db.AtomDB;
import com.dlvr.continuum.core.db.slice.NScan;
import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.db.slice.Scanner;
import com.dlvr.continuum.except.ZiggyStardustError;
import com.dlvr.continuum.io.file.Reference;
import com.dlvr.continuum.util.datetime.Interval;
import com.dlvr.continuum.db.Iterator;

import java.io.Closeable;
import java.util.*;

import static com.dlvr.continuum.util.Util.*;

/**
 * Root interface to library
 * Created by zack on 2/10/16.
 */
public class Continuum implements Closeable {

    private final String name;

    private final Dimension dimension;

    private final DB db;

    private final List<FileSystemReference> bases;

    public static void sayHello() {
        System.out.printf("People of Earth, how are you?\n");
    }

    public AtomBuilder atom() {
        return new AtomBuilder().dimension(dimension);
    }

    public AtomBuilder atom(String name) {
        return new AtomBuilder().dimension(dimension).name(name);
    }

    public static AtomBuilder satom() {
        return new AtomBuilder().dimension(Dimension.SPACE);
    }

    public static AtomBuilder katom() {
        return new AtomBuilder().dimension(Dimension.TIME);
    }

    public static ValuesBuilder values() {
        return new ValuesBuilder();
    }

    /**
     * Convenience method to create an initial values
     * @param value current value
     * @return values with all members set to values
     */
    public static Values values(double value) {
        return values(value, value, 1, value, value);
    }

    /**
     * Convenience method to create a Values
     * @param min minumum
     * @param max maximum
     * @param count count
     * @param sum sum
     * @param value current value
     * @return values with all members set to values
     */
    public static Values values(double min, double max, double count, double sum, double value) {
        return values().min(min).max(max).count(count).sum(sum).value(value).build();
    }

    public static Particles particles() {
        return new NParticles(new HashMap<>());
    }

    public static Particles particles(Map<String, String> particles) {
        return new NParticles(particles);
    }

    public static Particles particles(String... strings) {

        Map<String, String> tags = new HashMap<>(strings.length / 2);

        String tagName = null;
        for (String s : strings) {
            if (tagName != null) {
                if (s != null) tags.put(tagName, s);
                tagName = null;
            } else {
                tagName = s;
            }
        }

        return particles(tags);
    }

    public static Fields fields(Map<String, Object> fields) {
        return new NFields(fields);
    }

    public static Fields fields(Object... stringObject) {

        Map<String, Object> fields = new HashMap<>(stringObject.length / 2);

        String tagName = null;
        for (Object o : stringObject) {
            if (tagName != null) {
                fields.put(tagName, o);
                tagName = null;
            } else {
                tagName = (String)o;
            }
        }

        return fields(fields);
    }

    public static ScanBuilder scan() {
        return new ScanBuilder();
    }

    public ScanBuilder scan(String name) {
        return new ScanBuilder(name).dimension(dimension);
    }

    public static SliceBuilder result(String name) {
        return new SliceBuilder(name);
    }

    private Continuum(String name, Dimension dimension, List<FileSystemReference> bases) throws Exception {
        checkNotNull("name", name);
        checkNotNull("dimension", dimension);
        checkNotNull("base", bases);
        this.name = name;
        this.dimension = dimension;
        this.bases = bases;

        List<Slab> slabs = new ArrayList<>();
        for (int i = 0; i < bases.size(); i++) {
            Slab slab = new RockSlab(name + "." + i + ".db", bases.get(0));
            slabs.add(slab);
        }

        if (slabs.size() == 1) {
            this.db = new AtomDB(dimension, slabs.get(0));
        } else {
            this.db = new AtomDB(dimension, new Slabs(slabs));
        }
        this.db.open();
    }

    /**
     * Access the backing datastore for this continuum
     * @return DB datastore engine
     */
    public DB db() {
        return this.db;
    }

    /**
     * Write an atom to the time data store
     * @param atom to write or overwrite
     * @throws Exception error writing data
     */
    public void write(Atom atom) throws Exception {
        db().write(atom);
    }

    /**
     * Retreive a single atom by ID from the datastore
     * @param id to retrieve for
     * @return atom for given ID or null if not exist
     * @throws Exception on underlying slabs failure
     */
    Atom get(AtomID id) throws Exception {
        return db().read(id);
    }

    /**
     * Query: Execute a set of operations on a scan of time from the datastore
     * Blocking
     * @param scan description of the view of the slice
     * @return Slice of atoms resulting from the scan
     *          includes: aggregate functions, date ranges, and groupings if applicatble
     * @throws Exception error reading or collecting atoms
     */
    public Slice slice(Scan scan) throws Exception {
        Iterator iterator = db().iterator();
        try {
            Scanner scanner = scanner();
            scanner.iterator(iterator);
            return scanner.slice(scan);
        } finally {
            if (iterator != null) iterator.close();
        }
    }

    public Scanner scanner() {
        return new NScanner();
    }

    /**
     * Close all underlying resources for this continuum
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
     * @param scan time scan to compact
     * @throws Exception error contracting
     */
    public void contract(Scan scan) throws Exception {

    }

    /**
     * Expand a continuum (universe)? from cold storage data
     * @param reference incoming reference to add to this continuum
     * @throws Exception error expanding
     */
    public void expand(Reference reference) throws Exception {

    }

    public void delete(Scan scan) throws Exception {

    }

    public Reference clone(Scan scan) throws Exception {
        return null;
    }

    public static Builder series() {
        return new Builder().dimension(Dimension.SPACE);
    }

    public static Builder kv() {
        return new Builder().dimension(Dimension.TIME);
    }

    public static class Builder {
        private Dimension dimension = null;
        private List<FileSystemReference> base = new ArrayList<>();
        private String name = "continuum";
        public Builder name(String name) {
            this.name = name;
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
            return new Continuum(name, dimension, base);
        }
    }

    public static class ValuesBuilder {

        private NValues target = new NValues();

        public ValuesBuilder min(double min) {
            target.min = min;
            return this;
        }
        public ValuesBuilder max(double max) {
            target.max = max;
            return this;
        }
        public ValuesBuilder count(double count) {
            target.count = count;
            return this;
        }
        public ValuesBuilder sum(double sum) {
            target.sum = sum;
            return this;
        }
        public ValuesBuilder value(double value) {
            target.value = value;
            return this;
        }
        public Values build() {
            return target;
        }
    }

    public static class AtomBuilder {

        protected String name;
        protected NParticles particles;
        protected NFields fields;
        protected long timestamp = System.currentTimeMillis();
        protected NValues values;
        protected Dimension dimension;
        protected AtomBuilder() {}

        public AtomBuilder name(String name) {
            this.name = name;
            return this;
        }
        public AtomBuilder particles(Particles particles) {
            this.particles = (NParticles) particles;
            return this;
        }
        public AtomBuilder particles(Map<String, String> particles) {
            this.particles = (NParticles)Continuum.particles(particles);
            return this;
        }
        public AtomBuilder particles(String... particles) {
            this.particles = (NParticles)Continuum.particles(particles);
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
            this.values = (NValues)Continuum.values(value);
            return this;
        }
        public AtomBuilder values(Values values) {
            this.values = (NValues)values;
            return this;
        }
        public AtomBuilder dimension(Dimension dimension) {
            this.dimension = dimension;
            return this;
        }
        public Atom build() {
            if (dimension == Dimension.SPACE)
                return new SAtom(name, particles, timestamp, fields, values);
            else if (dimension == Dimension.TIME)
                return new KAtom(name, particles, timestamp, fields, values);
            throw new ZiggyStardustError();
        }
    }

    public static class ScanBuilder {

        private final NScan target = new NScan();

        private ScanBuilder() {
            this(null);
        }
        ScanBuilder(String name) {
            target.start = System.currentTimeMillis() - 1;
            target.end = target.start - Interval.valueOf("2d").millis();
            target.name = name;
        }
        public ScanBuilder name(String name) {
            target.name = name;
            return this;
        }
        public ScanBuilder start(long start) {
            target.start = start;
            return this;
        }
        public ScanBuilder start(Interval start) {
            target.start = System.currentTimeMillis() - start.millis();
            return this;
        }
        public ScanBuilder end(long end) {
            target.end = end;
            return this;
        }
        public ScanBuilder end(Interval end) {
            target.end = System.currentTimeMillis() - end.millis();
            return this;
        }
        public ScanBuilder interval(Interval interval) {
            target.interval = interval;
            return this;
        }
        public ScanBuilder function(Function function) {
            target.function = function;
            return this;
        }
        public ScanBuilder group(String group) {
            target.groups = new String[] { group };
            return this;
        }
        public ScanBuilder group(String... groups) {
            target.groups = groups;
            return this;
        }
        public ScanBuilder particles(Map<String, String> particles) {
            target.particles = Continuum.particles(particles);
            return this;
        }
        public ScanBuilder particles(Particles particles) {
            target.particles = particles;
            return this;
        }
        public ScanBuilder collectors(Collector... collectors) {
            target.collectors = collectors;
            return this;
        }
        public ScanBuilder fields(Fields fields) {
            target.fields = fields;
            return this;
        }
        public ScanBuilder dimension(Dimension dimension) {
            target.dimension = dimension;
            return this;
        }
        public Scan build() {
            return target;
        }
    }

    public static class SliceBuilder {
        private final NSlice target = new NSlice();
        private SliceBuilder() { }
        SliceBuilder(String name) {
            target.name = name;
        }
        public SliceBuilder value(Double value) {
            target.values = Continuum.values(value);
            return this;
        }
        public SliceBuilder values(Values values) {
            target.values = values;
            return this;
        }
        public SliceBuilder atoms(List<Atom> atoms) {
            target.atoms = atoms;
            return this;
        }
        public SliceBuilder children(List<Slice> children) {
            target.children = children;
            return this;
        }
        public SliceBuilder timestamp(long timestamp) {
            target.timestamp = timestamp;
            return this;
        }

        public Slice build() {
            return target;
        }
    }

    public enum Dimension {
        SPACE, TIME
     // Series / Key
    }
}
