package continuum;

import continuum.atom.Values;
import continuum.atom.Atom;
import continuum.atom.Fields;
import continuum.atom.Particles;
import continuum.control.Builder;
import continuum.control.Controller;
import continuum.core.atom.*;
import continuum.core.slab.AtomTranslator;
import continuum.core.slice.*;
import continuum.atom.AtomID;
import continuum.slab.Translator;
import continuum.slab.Slab;

import continuum.core.slab.Slabs;
import continuum.core.slab.RockSlab;
import continuum.core.io.file.FileSystemReference;
import continuum.except.ZiggyStardustError;
import continuum.file.Reference;
import continuum.slice.*;
import continuum.slice.Scanner;
import continuum.util.datetime.Interval;
import continuum.slab.Iterator;

import java.io.Closeable;
import java.util.*;
import java.util.stream.Stream;

import static continuum.slice.Const.LIMIT_NONE;
import static continuum.util.Util.*;

/**
 * Root interface to library
 * Created by zack on 2/10/16.
 */
public class Continuum implements Controller, Closeable {

    private final String name;

    // The DEFAULT dimension for atoms and scans on this continuum
    private final Dimension dimension;

    private final Translator<Atom> translator;

    private final Slabs slabs;

    private final List<Reference> bases;

    public static void sayHello() {
        System.out.printf("People of Earth, how are you?\n");
    }

    private Continuum(String name, Dimension dimension, List<Reference> bases) throws Exception {
        checkNotNull("name", name);
        checkNotNull("dimension", dimension);
        checkNotNull("base", bases);
        this.name = name;
        this.dimension = dimension;
        this.bases = bases;

        List<Slab> slabList = new ArrayList<>();
        for (int i = 0; i < bases.size(); i++) {
            FileSystemReference ref = (FileSystemReference)bases.get(0);
            Slab slab = new RockSlab(name + "." + i + ".slab", ref);
            slab.open();
            slabList.add(slab);
        }

        this.slabs = new Slabs(slabList);

        if (slabs.size() == 1) {
            this.translator = new AtomTranslator(dimension, slabs.get(0));
        } else {
            this.translator = new AtomTranslator(dimension, slabs);
        }
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
        return new AParticles(new HashMap<>());
    }

    public static Particles particles(Map<String, String> particles) {
        return new AParticles(particles);
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
        return new AFields(fields);
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

    public static SliceBuilder result(String name) {
        return new SliceBuilder(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AtomBuilder atom() {
        return new AtomBuilder().dimension(dimension);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AtomBuilder atom(String name) {
        return new AtomBuilder().dimension(dimension).name(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScanBuilder scan(String name) {
        return new ScanBuilder(name).dimension(dimension);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Translator<Atom> translator() {
        return this.translator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(Atom atom) throws Exception {
        translator().write(atom);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Atom get(AtomID id) throws Exception {
        return translator().read(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(AtomID id) throws Exception {
        System.out.println("Deleting: " + id.string());
        translator().delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Atom atom) throws Exception {
        delete(atom.ID());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Interval interval) throws Exception {
        delete(Const.SWILDCARD, interval);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String name, Interval interval) throws Exception {
        Scan scan = scan(name)
                .start(interval)
                .end(Interval.valueOf("1y"))
                .limit(1)
                .build();

        List<? extends Atom> atoms = slice(scan).atoms();

        while (atoms != null && atoms.size() > 0) {
            for (Atom atom : atoms)
                delete(atom);

            System.out.println("DONE");
            atoms = slice(scan).atoms();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Slice slice(Scan scan) throws Exception {
        Iterator iterator = translator().iterator();
        try {
            Scanner scanner = scanner();
            scanner.iterator(iterator);
            return scanner.slice(scan);
        } finally {
            if (iterator != null) iterator.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<Slice> stream(Scan scan) throws Exception {
        Iterator iterator = translator().iterator();
        Scanner scanner = scanner();
        scanner.iterator(iterator);
        return scanner.stream(scan);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scanner scanner() {
        return new AScanner();
    }

    /**
     * Close all underlying resources for this continuum
     */
    @Override
    public void close() {
        try {
            slabs.close();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * CAUTION: Deletes all fileystem resources associated with this continuum
     * @throws Exception on failure
     */
    public void delete() throws Exception {
        slabs.close();
        //bases.forEach(FileSystemReference::delete);
        for (Reference reference : bases) {
            reference.delete();
        }
    }

    public static ContinuumBuilder continuum() {
        return new ContinuumBuilder();
    }

    /**
     * Downsampling, compact atoms given a time range and size
     * @param scan time scan to compact
     * @throws Exception error contracting
     */
    public void contract(Scan scan) throws Exception { }

    /**
     * Expand a continuum (universe)? from cold storage data
     * @param reference incoming reference to add to this continuum
     * @throws Exception error expanding
     */
    public void expand(Reference reference) throws Exception { }

    public void delete(Scan scan) throws Exception { }

    public Reference clone(Scan scan) throws Exception { return null; }

    public static ContinuumBuilder series() {
        return new ContinuumBuilder().dimension(Dimension.SPACE);
    }

    public static ContinuumBuilder kv() {
        return new ContinuumBuilder().dimension(Dimension.TIME);
    }

    public static class ContinuumBuilder implements Builder {
        private Dimension dimension = null;
        private List<Reference> base = new ArrayList<>();
        private String name = "continuum";
        public ContinuumBuilder name(String name) {
            this.name = name;
            return this;
        }
        public ContinuumBuilder base(Reference base) {
            this.base.add(base);
            return this;
        }
        public ContinuumBuilder base(Reference... bases) {
            for (Reference ref : bases) {
                this.base.add(ref);
            }
            return this;
        }
        public ContinuumBuilder dimension(Dimension dimension) {
            this.dimension = dimension;
            return this;
        }
        public Continuum open() throws Exception {
            return new Continuum(name, dimension, base);
        }
    }

    public static class ValuesBuilder {

        private AValues target = new AValues();

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
        protected AParticles particles;
        protected AFields fields;
        protected long timestamp = System.currentTimeMillis();
        protected AValues values = new AValues();
        protected Dimension dimension;
        protected AtomBuilder() {}

        public AtomBuilder name(String name) {
            this.name = name;
            return this;
        }
        public AtomBuilder particles(Particles particles) {
            this.particles = (AParticles) particles;
            return this;
        }
        public AtomBuilder particles(Map<String, String> particles) {
            this.particles = (AParticles)Continuum.particles(particles);
            return this;
        }
        public AtomBuilder particles(String... particles) {
            this.particles = (AParticles)Continuum.particles(particles);
            return this;
        }
        public AtomBuilder fields(Fields fields) {
            this.fields = (AFields)fields;
            return this;
        }
        public AtomBuilder fields(Object... fields) {
            this.fields = (AFields)Continuum.fields(fields);
            return this;
        }
        public AtomBuilder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        public AtomBuilder value(double value) {
            this.values = (AValues)Continuum.values(value);
            return this;
        }
        public AtomBuilder values(Values values) {
            this.values = (AValues)values;
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

        private final AScan target = new AScan();

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
        public ScanBuilder collector(Collector collector) {
            target.collector = collector;
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
        public ScanBuilder limit(Integer limit) {
            if (limit != null && limit != LIMIT_NONE)
                target.limit = limit;
            return this;
        }
        public Scan build() {

            // add default collectors for this scan
            Collector defaul = Collectors.forScan(target);
            if (target.collector == null)
                target.collector = defaul;
            else if (target.collector instanceof AndCollector)
                 target.collector = new AndCollector(defaul,
                         ((AndCollector)target.collector).collectors());

            return target;
        }
    }

    public static class SliceBuilder {
        private final ASlice target = new ASlice();
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

    public static void main(String[] args) throws Exception {
        Continuum continuum = Continuum.continuum()
                .name("chewie-staging")
                .base(new FileSystemReference("/Users/zack/Desktop/chewie-staging"))
                .dimension(Dimension.SPACE)
                .open();


        continuum.delete("buffer_event", Interval.valueOf("5d"));
    }
}
