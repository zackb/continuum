package continuum.slice;

import continuum.atom.Fission;
import continuum.atom.Fusion;

/**
 * A special scanner which can do both data downsampling and some analysis (fission) along the way
 */
public interface CompactingScan extends Scan {
    Fusion fusion();
    Fission fission();
}
