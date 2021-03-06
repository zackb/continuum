package continuum.slab;

import continuum.atom.AtomID;

/**
 * Serde. Time data slab marshaller / demarshaller.
 */
public interface Translator<T> {

    /**
     * Write a datapoint to the slab storage
     * @param t atom to write
     * @throws Exception error writing atom
     */
    void write(T t) throws Exception;

    /**
     * Retreive a single atom by ID from the datastore
     * @param id to retrieve for
     * @return atom for given ID or null if not exist
     * @throws Exception on underlying slabs failure
     */
    T read(AtomID id) throws Exception;

    /**
     * Delete a datapoint from slab storage
     * @param id atom id to delete
     * @throws Exception error deleting atom
     */
    void delete(AtomID id) throws Exception;

    /**
     * Get an iterator for the data store.
     * WARN Caller MUST Call close()
     * Available exclusively to caller until close() is called.
     * @return new iterator
     */
    Iterator<T> iterator();

    /**
     * Get an iterator for the data store.
     * WARN Caller MUST Call close()
     * Available exclusively to caller until close() is called.
     * @return new iterator
     */
    Iterator<T> iterator(boolean streaming);

    /**
     * The underlying slab storage resource for this translator
     * @return this slab storage
     */
    Slab slab();
}