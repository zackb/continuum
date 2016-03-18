package continuum.slice;

import continuum.atom.Atom;
import continuum.atom.Values;

import java.util.List;

/**
 * Slice of space time.
 * A view of space time data
 */
public interface Slice {

    /**
     * Name to scan (series or key)
     * @return name to scan
     */
    String name();

    /**
     * Optional values to filter on
     * @return values that results should adhere to
     */
    Values values();

    /**
     * Atoms matching filters scan filters
     * @return atom data
     */
    List<? extends Atom> atoms();

    /**
     * Timestamp for this slice
     * @return timestamp
     */
    long timestamp();

    /**
     * Sub-slices of this slice for grouping and time intervals
     * @return sub-slices
     */
    List<Slice> slices();

    /**
     * Get a sub-slice by name
     * @param name of sub-slice
     * @return sub-slice
     */
    Slice slice(String name);

    /**
     * Get a sub-slice by index
     * @param index sub-slice position
     * @return sub-slice at the given position
     */
    Slice slice(int index);

    /**
     * Add a sub-slice to this slice
     * @param result sub-slice to add
     * @return sub-slice that was added
     */
    Slice add(Slice result);

    /**
     * Remoce a sub-slice from this slice
     * @param result sub-slice to remove
     * @return sub-slice that was removed
     */
    Slice remove(Slice result);
}
