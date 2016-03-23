package continuum.core.slice;

import continuum.atom.Atom;
import continuum.atom.Values;
import continuum.slice.Slice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Tree structured results from a translator scan
 */
public class ASlice implements Slice {

    public String name;
    public Values values;
    public long timestamp;
    public List<Slice> children;
    public List<Atom> atoms;

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
    public List<Slice> slices() {
        return children;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Atom> atoms() {
        return atoms;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice add(Slice result) {
        if (children == null) children = new ArrayList<>();
        children.add(result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice remove(Slice result) {
        if (children == null) return result;
        children.remove(result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice slice(String name) {
        for (Slice res : children) {
            if (res.name().equals(name)) {
                return res;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice slice(int idx) {
        return children.get(idx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long timestamp() {
        return timestamp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ASlice)) return false;

        ASlice that = (ASlice) o;

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

    @Override
    public Stream<Slice> flattened() {
        return children == null ? stream() : Stream.concat(
                this.stream(),
                children.stream()
                        .flatMap(Slice::flattened));
    }

    @Override
    public Stream<Slice> stream() {
        return Stream.of(this);
    }
}
