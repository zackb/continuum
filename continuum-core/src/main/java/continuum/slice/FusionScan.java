package continuum.slice;

import continuum.atom.Fusion;

/**
 * Interface to scans which "compact" data or turn many data points into one
 */
public interface FusionScan extends Scan {
    Fusion fusion();
}
