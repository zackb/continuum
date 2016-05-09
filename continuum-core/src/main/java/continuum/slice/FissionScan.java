package continuum.slice;

import continuum.atom.Fission;

/**
 * A scan which turns space or time data into different space or time data.
 * Can be used to create new series from a collection of related data points
 */
public interface FissionScan extends Scan {
    Fission fission();
}
