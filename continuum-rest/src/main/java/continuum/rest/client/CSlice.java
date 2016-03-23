package continuum.rest.client;


import continuum.atom.Atom;
import continuum.atom.Values;
import continuum.core.atom.AValues;
import continuum.core.atom.SAtom;
import continuum.slice.Slice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * POJO version of a slice result
 * Created by zack on 2/26/16.
 */
public class CSlice implements Slice {

    public String name;
    public AValues values;
    public List<CSlice> children;
    public List<SAtom> atoms;
    public long timestamp;

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Values values() {
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Slice> slices() {
        return children;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Atom> atoms() {
        return atoms;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice add(Slice slice) {
        if (children == null) children = new ArrayList<>();
        children.add((CSlice)slice);
        return slice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice remove(Slice slice) {
        if (children != null)
            children.remove(slice);
        return slice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice slice(String name) {

        if (children == null) return null;

        for (Slice slice : children)
            if (slice.name().equals(name))
                return slice;

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice slice(int i) {
        return children.get(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long timestamp() {
        return timestamp;
    }

    @Override
    public Stream<Slice> flattened() {
        return children == null ? stream() : Stream.concat(
                this.stream(),
                children.stream()
                        .flatMap(CSlice::flattened)
        );
    }

    @Override
    public Stream<Slice> stream() {
        return Stream.of(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CSlice)) return false;

        CSlice that = (CSlice) o;

        if (timestamp != that.timestamp) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (values != null ? !values.equals(that.values) : that.values != null) return false;
        if (children != null ? !children.equals(that.children) : that.children != null) return false;
        if (atoms != null ? !atoms.equals(that.atoms) : that.atoms != null) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (values != null ? values.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + (atoms != null ? atoms.hashCode() : 0);
        return result;
    }
}
