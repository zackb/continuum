package continuum.slice;

import continuum.atom.Fields;
import continuum.atom.Particles;
import continuum.util.datetime.Interval;

import static continuum.Continuum.Dimension;

/**
 * Scan over space time
 */
public interface Scan {

    /**
     * Non-unique ID for this scan
     * @return ID used to begin slab seek
     */
    ScanID ID();

    /**
     * The dimension to scan on, space (name) or time (timestamp)
     */
    Dimension dimension();

    /**
     * The measurement name to scan for
     * @return measurement name
     */
    String name();

    /**
     * Tag names and values to filter on. Or WILDCARD
     * Tags should be used for optimized queries
     * @return particles values to filter on
     */
    Particles particles();

    /**
     * Field names and values to filter on.
     * Fields should be used infrequently to filter on
     * @return field values to filter on
     */
    Fields fields();

    /**
     * Aggregation function for slice's 'values' field.
     * If non-null, scan results are aggreagate values.
     * If null, scan results will have raw measurement values
     * @return aggregation function
     */
    Function function();

    /**
     * Timestamp to begin scan range (the smallest timestamp), expresed in millisecond epoch
     * @return unix epoch in milliseconds
     */
    long start();

    /**
     * Timestamp to end scan range (the largest timestamp), expresed in millisecond epoch
     * @return unix epoch in milliseconds
     */
    long end();

    /**
     * Date range interval to bucket results
     * If non-null, scan results are bucketed into date ranges using the {#getFunction()} aggreagate function
     * If null, scan results are atom-in-time values
     * @return time unit
     */
    Interval interval();

    /**
     * Result groupings on particles
     * @return particle names to group by
     */
    String[] groups();


    /**
     * User supplied Collector to add to the scan in addition to the standard group,interval,values,etc
     * @return optional user defined collector
     */
    Collector collector();


    /**
     * User supplied Filter to add to the scan in addition to any implied filters on timestamps, particles, and fields
     * @return optional user defined filter
     */
    Filter filter();
}